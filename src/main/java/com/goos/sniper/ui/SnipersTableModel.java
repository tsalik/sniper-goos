package com.goos.sniper.ui;

import com.goos.sniper.Main;
import com.goos.sniper.sniper.Joining;
import com.goos.sniper.sniper.SniperSnapshot;
import com.goos.sniper.sniper.SniperSnapshotUtils;

import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel {

    private String status = Main.STATUS_JOINING;
    private static final SniperSnapshot STARTING_UP = SniperSnapshotUtils.joining("");
    private SniperSnapshot state = STARTING_UP;

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (Column.at(columnIndex)) {
            case ITEM_IDENTIFIER:
                return state.getItemId();
            case LAST_PRICE:
                return state.getLastPrice();
            case LAST_BID:
                return state.getLastBid();
            case SNIPER_STATUS:
                return status;
            default:
                throw new IllegalStateException("Could not find column at: " + columnIndex);
        }
    }

    void setStatusText(String status) {
        this.status = status;
        fireTableRowsUpdated(0, 0);
    }

    public void sniperStateChanged(SniperSnapshot state) {
        this.state = state;
        this.status = state.getSniperState().status();
        fireTableRowsUpdated(0, 0);
    }

}
