package com.goos.sniper.xmpp;

import com.goos.sniper.sniper.Auction;
import com.goos.sniper.sniper.AuctionHouse;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XMPPAuctionHouse implements AuctionHouse {

    private final XMPPConnection connection;

    private XMPPAuctionHouse(XMPPConnection connection) {
        this.connection = connection;
    }

    public static XMPPAuctionHouse connect(String hostName, String userName, String password) throws XMPPException {
        return new XMPPAuctionHouse(connection(hostName, userName, password));
    }

    @Override
    public Auction auctionFor(String itemId) {
        return new XMPPAuction(connection, itemId);
    }

    @Override
    public void disconnect() {
        connection.disconnect();
    }

    private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, XMPPAuction.AUCTION_RESOURCE);
        return connection;
    }

}
