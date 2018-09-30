package e2e;

import org.junit.After;
import org.junit.Test;

public class AuctionSniperEndToEndTest {

    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
    private final ApplicationRunner application = new ApplicationRunner();

    @Test
    public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
        auction.startSellingItem();
        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        auction.announceClosed();
        application.showsSniperHasLostAuction();
    }

    @Test
    public void sniperMakesAHigherBidButLoses() throws Exception {
        auction.startSellingItem();

        application.startBiddingIn(auction);
        // (1) Wait to check that the Join was received and to synchronize the Sniper with the auction
        auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

        // (2) Send a message back that at the moment the current price is 1000, next bid is 98 and the winner is other bidder
        auction.reportPrice(1000, 98, "other bidder");
        // (3) Check that the application shows it's bidding after it received the Price event
        application.hasShownSniperIsBidding();

        /* (4) Check that the auction received the bid from the sniper
         * XMPP creates an id with the name of the sniper, the host and the resource
         */
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);

        auction.announceClosed();
        application.showsSniperHasLostAuction();
    }

    @After
    public void stopAuction() {
        auction.stop();
    }

    @After
    public void stopApplication() {
        application.stop();
    }

}
