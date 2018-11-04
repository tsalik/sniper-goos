package com.goos.sniper;

import com.goos.sniper.sniper.Auction;
import com.goos.sniper.sniper.AuctionSniper;
import com.goos.sniper.sniper.SniperSnapshotUtils;
import com.goos.sniper.ui.MainWindow;
import com.goos.sniper.ui.SnipersTableModel;
import com.goos.sniper.ui.SwingThreadSniperListener;
import com.goos.sniper.xmpp.XMPPAuctionHouse;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    public static final String STATUS_JOINING = "Joining";
    public static final String STATUS_LOST = "Lost";
    public static final String STATUS_BIDDING = "Bidding";
    public static final String STATUS_WINNING = "Winning";
    public static final String STATUS_WON = "Won";


    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;

    public static final String JOIN_COMMAND_FORMAT = "SOLVersion 1.1; Command: Join";
    public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d";

    private final SnipersTableModel snipers = new SnipersTableModel();

    private MainWindow mainWindow;
    private Auction notToBeGCd;

    private Main() throws Exception {
        startUserInterface();
    }

    public static void main(String... args) throws Exception {
        Main main = new Main();
        XMPPAuctionHouse auctionHouse = XMPPAuctionHouse.connect(
                args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
        main.disconnectWhenUICloses(auctionHouse);
        main.addUserRequestListener(auctionHouse);
    }

    private void addUserRequestListener(XMPPAuctionHouse auctionHouse) {
        mainWindow.addUserRequestListener(itemId -> {
            snipers.addSniper(SniperSnapshotUtils.joining(itemId));

            Auction auction = auctionHouse.auctionFor(itemId);
            notToBeGCd = auction;
            auction.addAuctionEventListener(
                    new AuctionSniper(itemId, auction,
                            new SwingThreadSniperListener(snipers)));

            auction.join();
        });
    }

    private void disconnectWhenUICloses(XMPPAuctionHouse auctionHouse) {
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                auctionHouse.disconnect();
            }
        });
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(() -> mainWindow = new MainWindow(snipers));
    }

}
