package com.goos.sniper.ui;

import com.goos.sniper.sniper.SniperListener;
import com.goos.sniper.sniper.SniperSnapshot;
import com.goos.sniper.ui.SnipersTableModel;

import javax.swing.*;

public class SwingThreadSniperListener implements SniperListener {

    private final SnipersTableModel sniper;

    public SwingThreadSniperListener(SnipersTableModel snipers) {
        this.sniper = snipers;
    }

    @Override
    public void sniperStateChanged(SniperSnapshot sniperSnapshot) {
        SwingUtilities.invokeLater(() -> sniper.sniperStateChanged(sniperSnapshot));
    }

}
