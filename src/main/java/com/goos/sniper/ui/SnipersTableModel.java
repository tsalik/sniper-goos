package com.goos.sniper.ui;

import com.goos.sniper.sniper.SniperSnapshot;
import com.goos.sniper.sniper.SniperSnapshotUtils;

import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel {

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
    public String getColumnName(int column) {
        return Column.at(column).name;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(state);
    }

    public void sniperStateChanged(SniperSnapshot state) {
        this.state = state;
        fireTableRowsUpdated(0, 0);
    }

}
