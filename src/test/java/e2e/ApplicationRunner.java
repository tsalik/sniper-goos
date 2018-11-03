package e2e;

import com.goos.sniper.Main;
import com.goos.sniper.sniper.*;

import static e2e.FakeAuctionServer.XMPP_HOSTNAME;

class ApplicationRunner {

    static final String SNIPER_XMPP_ID = "sniper@localhost/Auction";
    private static final String SNIPER_ID = "sniper";
    private static final String SNIPER_PASSWORD = "sniper";

    private AuctionSniperDriver driver;

    void startBiddingIn(FakeAuctionServer... auctions) {
        startSniper(auctions);
        for (FakeAuctionServer auction : auctions) {
            final String itemId = auction.getItemId();
            driver.startBiddingFor(itemId);
            driver.showsSniperStatus(itemId, 0, 0, textFor(Joining.INSTANCE));
        }
    }

    void showsSniperHasLostAuction(String itemId, int lastPrice, int lastBid) {
        // (6) We expect that the sniper has lost the auction show it should be showing somewhere a label with Lost
        driver.showsSniperStatus(itemId, lastPrice, lastBid, textFor(Lost.INSTANCE));
    }

    void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, textFor(Bidding.INSTANCE));
    }

    void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
        driver.showsSniperStatus(auction.getItemId(), winningBid, winningBid, textFor(Winning.INSTANCE));
    }

    void showsSniperHasWonAuction(FakeAuctionServer auction, int lastPrice) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastPrice, textFor(Won.INSTANCE));
    }

    void stop() {
        if (driver != null) {
            /* (7) After the test has finished, tell the driver to dispose the window so it won't be picked by
             * another test before being garbage-collected
             */
            driver.dispose();
        }
    }

    private void startSniper(FakeAuctionServer[] auctions) {
        // (1) Run the Sniper in a new Thread, ideally we would in a new process, reasonable compromise
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    // (2) Assume we're bidding for one item only
                    Main.main(arguments(auctions));
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
    }

    private String[] arguments(FakeAuctionServer... auctions) {
        String[] arguments = new String[auctions.length + 3];
        arguments[0] = XMPP_HOSTNAME;
        arguments[1] = SNIPER_ID;
        arguments[2] = SNIPER_PASSWORD;
        for (int i = 0; i < auctions.length; i++) {
            arguments[i + 3] = auctions[i].getItemId();
        }
        return arguments;
    }

    private String textFor(SniperState sniperState) {
        return sniperState.status();
    }

}
