package e2e;

import com.goos.sniper.Main;

import static com.goos.sniper.Main.*;
import static e2e.FakeAuctionServer.XMPP_HOSTNAME;

class ApplicationRunner {

    static final String SNIPER_XMPP_ID = "sniper@localhost/Auction";
    private static final String SNIPER_ID = "sniper";
    private static final String SNIPER_PASSWORD = "sniper";
    private String itemId;

    private AuctionSniperDriver driver;

    void startBiddingIn(FakeAuctionServer auction) {
        itemId = auction.getItemId();
        // (1) Run the Sniper in a new Thread, ideally we would in a new process, reasonable compromise
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    // (2) Assume we're bidding for one item only
                    Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
                } catch (Exception e) {
                    // (3) Just print the stack trace until we handle exceptions properly
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        // (4) Turn the timeout down to find components
        driver = new AuctionSniperDriver(1000);
        // (5) We expect the app to show somewhere that we are joining the auction
        driver.hasTitle("Auction Sniper");
        driver.hasColumnTitles();
        driver.showsSniperStatus(itemId, 0, 0, STATUS_JOINING);
    }

    void showsSniperHasLostAuction(String itemId, int lastPrice, int lastBid) {
        // (6) We expect that the sniper has lost the auction show it should be showing somewhere a label with Lost
        driver.showsSniperStatus(itemId, lastPrice, lastBid, STATUS_LOST);
    }

    void hasShownSniperIsBidding(int lastPrice, int lastBid) {
        driver.showsSniperStatus(itemId, lastPrice, lastBid, STATUS_BIDDING);
    }

    void hasShownSniperIsWinning(int winningBid) {
        driver.showsSniperStatus(itemId, winningBid, winningBid, STATUS_WINNING);
    }

    void hasShownSniperHasWonTheAuction(int lastPrice) {
        driver.showsSniperStatus(itemId, lastPrice, lastPrice, STATUS_WON);
    }

    void stop() {
        if (driver != null) {
            /* (7) After the test has finished, tell the driver to dispose the window so it won't be picked by
             * another test before being garbage-collected
             */
            driver.dispose();
        }
    }
}
