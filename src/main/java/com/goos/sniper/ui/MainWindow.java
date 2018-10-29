package com.goos.sniper.ui;

import com.goos.sniper.sniper.SniperSnapshot;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

import static com.goos.sniper.Main.STATUS_JOINING;

public class MainWindow extends JFrame {

    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String SNIPER_STATUS_NAME = "sniper status";
    private static final String SNIPERS_TABLE_NAME = "snipers";
    private final SnipersTableModel snipers = new SnipersTableModel();

    private final JLabel sniperStatus = createLabel(STATUS_JOINING);

    public MainWindow() {
        super("Auction Sniper");
        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JLabel createLabel(String initialText) {
        JLabel result = new JLabel(initialText);
        result.setName(SNIPER_STATUS_NAME);
        result.setBorder(new LineBorder(Color.BLACK));
        return result;
    }

    private void fillContentPane(JTable snipersTable) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable() {
        final JTable snipersTable = new JTable(snipers);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }

    public void showStatusText(String status) {
        snipers.setStatusText(status);
    }

    public void sniperStateChanged(SniperSnapshot state) {
        snipers.sniperStateChanged(state);
    }

}
