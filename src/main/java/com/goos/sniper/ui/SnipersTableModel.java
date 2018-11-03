package com.goos.sniper.ui;

import com.goos.sniper.sniper.SniperSnapshot;
import kotlin.Pair;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class SnipersTableModel extends AbstractTableModel {

    private List<SniperSnapshot> snipers = new ArrayList<>();

    @Override
    public int getRowCount() {
        return snipers.size();
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
        return Column.at(columnIndex).valueIn(snipers.get(rowIndex));
    }

    public void sniperStateChanged(SniperSnapshot state) {
        Pair<Boolean, Integer> pair = isForSameItemAs(state);
        final boolean sniperExits = pair.getFirst();
        final int position = pair.getSecond();
        if (sniperExits) {
            snipers.set(pair.getSecond(), state);
            fireTableRowsUpdated(position, position);
        }
    }

    private Pair<Boolean, Integer> isForSameItemAs(SniperSnapshot snapshot) {
        final int notFound = -1;
        for (int i = 0; i < snipers.size(); i++) {
            SniperSnapshot state = snipers.get(i);
            if (state.getItemId().equals(snapshot.getItemId())) {
                return new Pair<>(true, i);
            }
        }
        return new Pair<>(false, notFound);
    }

    public void addSniper(SniperSnapshot snapshot) {
        snipers.add(snapshot);
        final int insertedAt = snipers.size();
        fireTableRowsInserted(insertedAt, insertedAt);
    }

}
