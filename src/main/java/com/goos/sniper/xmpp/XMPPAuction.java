package com.goos.sniper.xmpp;

import com.goos.sniper.Main;
import com.goos.sniper.sniper.Auction;
import com.goos.sniper.sniper.AuctionEventListener;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XMPPAuction implements Auction {

    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    private final Chat chat;
    private final AuctionMessageTranslator messageTranslator;

    public XMPPAuction(XMPPConnection connection, String itemId) {
        messageTranslator = new AuctionMessageTranslator(connection.getUser(), null);
        chat = connection.getChatManager().createChat(auctionId(itemId, connection), messageTranslator);
    }

    @Override
    public void join() {
        sendMessage(Main.JOIN_COMMAND_FORMAT);
    }

    @Override
    public void bid(int amount) {
        sendMessage(String.format(Main.BID_COMMAND_FORMAT, amount));
    }

    @Override
    public void addAuctionEventListener(AuctionEventListener auctionEventListener) {
        messageTranslator.addAuctionEventListener(auctionEventListener);
    }

    private void sendMessage(String format) {
        try {
            chat.sendMessage(format);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }

}
