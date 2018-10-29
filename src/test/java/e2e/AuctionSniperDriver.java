package e2e;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.driver.JTableHeaderDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

import javax.swing.table.JTableHeader;

import static com.goos.sniper.ui.MainWindow.MAIN_WINDOW_NAME;
import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;

@SuppressWarnings("unchecked")
class AuctionSniperDriver extends JFrameDriver {

    AuctionSniperDriver(int timeoutMillis) {
        super(new GesturePerformer(),
                JFrameDriver.topLevelFrame(
                        named(MAIN_WINDOW_NAME),
                        showingOnScreen()
                ), new AWTEventQueueProber(timeoutMillis, 100));
    }

    void showsSniperStatus(String itemId, int lastPrice, int lastBid, String status) {
        new JTableDriver(this).hasRow(matching(
                withLabelText(itemId),
                withLabelText(String.valueOf(lastPrice)),
                withLabelText(String.valueOf(lastBid)),
                withLabelText(status)
        ));
    }

    void hasColumnTitles() {
        JTableHeaderDriver headers = new JTableHeaderDriver(this, JTableHeader.class);
        headers.hasHeaders(matching(
                withLabelText("Item"),
                withLabelText("Last Price"),
                withLabelText("Last Bid"),
                withLabelText("State")
        ));
    }
}
