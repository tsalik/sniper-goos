package unit;


import com.goos.sniper.sniper.Bidding;
import com.goos.sniper.sniper.SniperSnapshot;
import com.goos.sniper.sniper.SniperSnapshotUtils;
import com.goos.sniper.ui.Column;
import com.goos.sniper.ui.SnipersTableModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SnipersTableModelTest {

    private final SnipersTableModel model = new SnipersTableModel();
    private final TableModelListener listener = mock(TableModelListener.class);
    private final ArgumentCaptor<TableModelEvent> tableEventCaptor = ArgumentCaptor.forClass(TableModelEvent.class);

    @Before
    public void attachListener() {
        model.addTableModelListener(listener);
    }

    @Test
    public void hasEnoughColumns() {
        assertThat(model.getColumnCount(), equalTo(4));
    }

    @Test
    public void setsSniperValuesInColumns() {

        model.addSniper(SniperSnapshotUtils.joining("itemId"));
        model.sniperStateChanged(new SniperSnapshot("itemId", 555, 666, Bidding.INSTANCE));

        assertRowMatchesSnapshot(0, new SniperSnapshot("itemId", 555, 666, Bidding.INSTANCE));

        verify(listener, atLeastOnce()).tableChanged(tableEventCaptor.capture());
        assertThat(tableEventCaptor.getValue(), samePropertyValuesAs(new TableModelEvent(model, 0)));
    }

    @Test
    public void notifiesListenersWhenAddingASniper() {
        SniperSnapshot joining = SniperSnapshotUtils.joining("item123");

        assertEquals(0, model.getRowCount());
        model.addSniper(joining);
        assertEquals(1, model.getRowCount());

        assertRowMatchesSnapshot(0, joining);

        verify(listener).tableChanged(tableEventCaptor.capture());
    }

    @Test
    public void holdsSnipersInAdditionOrder() {

        model.addSniper(SniperSnapshotUtils.joining("item 0"));
        model.addSniper(SniperSnapshotUtils.joining("item 1"));

        assertRowMatchesSnapshot(0, SniperSnapshotUtils.joining("item 0"));
        assertRowMatchesSnapshot(1, SniperSnapshotUtils.joining("item 1"));
    }

    private void assertRowMatchesSnapshot(int rowIndex, SniperSnapshot sniper) {
        assertColumnEquals(rowIndex, Column.ITEM_IDENTIFIER, sniper.getItemId());
        assertColumnEquals(rowIndex, Column.LAST_PRICE, sniper.getLastPrice());
        assertColumnEquals(rowIndex, Column.LAST_BID, sniper.getLastBid());
        assertColumnEquals(rowIndex, Column.SNIPER_STATUS, sniper.getSniperState().status());
    }

    private void assertColumnEquals(int rowIndex, Column column, Object expected) {
        int columnIndex = column.ordinal();
        assertThat(model.getValueAt(rowIndex, columnIndex), equalTo(expected));
    }
}
