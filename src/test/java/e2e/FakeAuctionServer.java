package e2e;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import static com.goos.sniper.Main.AUCTION_RESOURCE;
import static com.goos.sniper.Main.ITEM_ID_AS_LOGIN;

class FakeAuctionServer {

    static final String XMPP_HOSTNAME = "localhost";
    private static final String AUCTION_PASSWORD = "auction";

    private final SingleMessageListener messageListener = new SingleMessageListener();
    private final String itemId;
    private final XMPPConnection connection;
    private Chat currentChat;

    public FakeAuctionServer(String itemId) {
        this.itemId = itemId;
        this.connection = new XMPPConnection(XMPP_HOSTNAME);
    }

    public String getItemId() {
        return itemId;
    }

    public void startSellingItem() throws XMPPException {
        connection.connect();
        connection.login(String.format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE);
        connection.getChatManager().addChatListener((chat, createdLocally) -> {
            currentChat = chat;
            chat.addMessageListener(messageListener);
        });
    }

    public void hasReceivedJoinRequestFromSniper() throws InterruptedException {
        /* (1) We need to check that a Join has arrived.
         * Since we implement only Join for the moment any message arrived will do
         */
        messageListener.receivesAMessage();
    }

    void announceClosed() throws XMPPException {
        // (2) Like the Join request, any Message that we send will do for now
        currentChat.sendMessage(new Message());
    }

    void stop() {
        connection.disconnect();
    }


}
