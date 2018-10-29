package unit;


import com.goos.sniper.sniper.Bidding;
import com.goos.sniper.sniper.SniperSnapshot;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SnipersTableModelTest {

    private final SnipersTableModel snipersTableModel = new SnipersTableModel();
    private final TableModelListener listener = mock(TableModelListener.class);
    private final ArgumentCaptor<TableModelEvent> tableEventCaptor = ArgumentCaptor.forClass(TableModelEvent.class);

    @Before
    public void attachListener() {
        snipersTableModel.addTableModelListener(listener);
    }

    @Test
    public void hasEnoughColumns() {
        assertThat(snipersTableModel.getColumnCount(), equalTo(4));
    }

    @Test
    public void setsSniperValuesInColumns() {

        snipersTableModel.sniperStateChanged(new SniperSnapshot("itemId", 555, 666, Bidding.INSTANCE));

        assertColumnEquals(Column.ITEM_IDENTIFIER, "itemId");
        assertColumnEquals(Column.LAST_PRICE, 555);
        assertColumnEquals(Column.LAST_BID, 666);
        assertColumnEquals(Column.SNIPER_STATUS, "Bidding");

        verify(listener).tableChanged(tableEventCaptor.capture());
        assertThat(tableEventCaptor.getValue(), samePropertyValuesAs(new TableModelEvent(snipersTableModel, 0)));
    }

    private void assertColumnEquals(Column column, Object expected) {
        int rowIndex = 0;
        int columnIndex = column.ordinal();
        assertThat(snipersTableModel.getValueAt(rowIndex, columnIndex), equalTo(expected));
    }
}
