package com.goos.sniper;

import com.goos.sniper.sniper.Auction;
import com.goos.sniper.sniper.AuctionSniper;
import com.goos.sniper.sniper.SniperListener;
import com.goos.sniper.xmpp.AuctionMessageTranslator;
import com.goos.sniper.xmpp.XMPPAuction;
import com.goos.sniper.ui.MainWindow;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    public static final String STATUS_JOINING = "Joining";
    public static final String STATUS_LOST = "Lost";
    public static final String STATUS_BIDDING = "Bidding";
    public static final String STATUS_WINNING = "Winning";
    public static final String STATUS_WON = "Won";

    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";

    private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private static final int ARG_ITEM_ID = 3;

    public static final String JOIN_COMMAND_FORMAT = "SOLVersion 1.1; Command: Join";
    public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d";

    private MainWindow ui;
    private Chat notToBeGCd;

    private Main() throws Exception {
        startUserInterface();
    }

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.joinAuction(connection(
                args[ARG_HOSTNAME],
                args[ARG_USERNAME],
                args[ARG_PASSWORD]), args[ARG_ITEM_ID]);

    }

    private void joinAuction(XMPPConnection connection, String itemId) {
        disconnectWhenUICloses(connection);
        final Chat chat = connection.getChatManager().createChat(auctionId(itemId, connection), null);
        this.notToBeGCd = chat;

        Auction auction = new XMPPAuction(chat);
        chat.addMessageListener(new AuctionMessageTranslator(connection.getUser(), new AuctionSniper(auction, new SniperStateDisplayer())));
        auction.join();
    }

    private void disconnectWhenUICloses(XMPPConnection connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                connection.disconnect();
            }
        });
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }

    private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);
        return connection;
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(() -> ui = new MainWindow());
    }

    public class SniperStateDisplayer implements SniperListener {

        @Override
        public void sniperLost() {
            showStatus(STATUS_LOST);
        }

        @Override
        public void sniperBidding() {
            showStatus(STATUS_BIDDING);
        }

        @Override
        public void sniperWinning() {
            showStatus(STATUS_WINNING);
        }

        @Override
        public void sniperWon() {
            showStatus(STATUS_WON);
        }

        private void showStatus(String status) {
            SwingUtilities.invokeLater(() -> ui.showStatus(status));
        }

    }

}
