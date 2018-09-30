package e2e;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

import static com.goos.sniper.ui.MainWindow.MAIN_WINDOW_NAME;
import static com.goos.sniper.ui.MainWindow.SNIPER_STATUS_NAME;
import static org.hamcrest.Matchers.equalTo;

@SuppressWarnings("unchecked")
class AuctionSniperDriver extends JFrameDriver {

    AuctionSniperDriver(int timeoutMillis) {
        super(new GesturePerformer(),
                JFrameDriver.topLevelFrame(
                        named(MAIN_WINDOW_NAME),
                        showingOnScreen()
                ), new AWTEventQueueProber(timeoutMillis, 100));
    }

    void showsSniperStatus(String statusText) {
        new JLabelDriver(this, named(SNIPER_STATUS_NAME)).hasText(equalTo(statusText));
    }

}
