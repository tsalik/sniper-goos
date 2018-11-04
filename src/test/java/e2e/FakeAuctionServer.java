package e2e;

import com.goos.sniper.Main;
import org.hamcrest.Matcher;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import static com.goos.sniper.xmpp.XMPPAuction.AUCTION_RESOURCE;
import static com.goos.sniper.xmpp.XMPPAuction.ITEM_ID_AS_LOGIN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class FakeAuctionServer {

    static final String XMPP_HOSTNAME = "localhost";
    private static final String AUCTION_PASSWORD = "auction";

    private final SingleMessageListener messageListener = new SingleMessageListener();
    private final String itemId;
    private final XMPPConnection connection;
    private Chat currentChat;

    FakeAuctionServer(String itemId) {
        this.itemId = itemId;
        this.connection = new XMPPConnection(XMPP_HOSTNAME);
    }

    String getItemId() {
        return itemId;
    }

    void startSellingItem() throws XMPPException {
        connection.connect();
        connection.login(String.format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE);
        connection.getChatManager().addChatListener((chat, createdLocally) -> {
            currentChat = chat;
            chat.addMessageListener(messageListener);
        });
    }

    void announceClosed() throws XMPPException {
        // (2) Like the Join request, any Message that we send will do for now
        currentChat.sendMessage("SOLVersion: 1.1; Event: CLOSE;");
    }

    void reportPrice(int price, int increment, String bidder) throws XMPPException {
        currentChat.sendMessage(String.format(
                "SOLVersion: 1.1; Event: PRICE; CurrentPrice: %d; Increment: %d; Bidder: %s", price, increment, bidder
        ));
    }

    void hasReceivedJoinRequestFrom(String sniperId) throws InterruptedException {
        /* (1) We need to check that a Join has arrived.
         * Since we implement only Join for the moment any message arrived will do
         */
        receivesAMessageMatching(sniperId, equalTo(Main.JOIN_COMMAND_FORMAT));
    }

    void hasReceivedBid(int bid, String sniperId) throws InterruptedException {
        assertThat(currentChat.getParticipant(), equalTo(sniperId));
        messageListener.receivesAMessage(equalTo(String.format(Main.BID_COMMAND_FORMAT, bid)));
    }

    private void receivesAMessageMatching(String sniperId, Matcher<? super String> messageMatcher) throws InterruptedException {
        messageListener.receivesAMessage(messageMatcher);
        assertThat(currentChat.getParticipant(), equalTo(sniperId));
    }

    void stop() {
        connection.disconnect();
    }
}
