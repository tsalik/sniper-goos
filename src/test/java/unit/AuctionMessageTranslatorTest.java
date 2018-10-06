package unit;

import com.goos.sniper.sniper.AuctionEventListener;
import com.goos.sniper.xmpp.AuctionMessageTranslator;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AuctionMessageTranslatorTest {

    private static final Chat UNUSED_CHAT = null;

    private final AuctionEventListener eventListener = mock(AuctionEventListener.class);
    private final AuctionMessageTranslator translator = new AuctionMessageTranslator(eventListener);

    @Test
    public void notifiesAuctionClosedWhenCloseMessageReceived() {
        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: CLOSE;");

        translator.processMessage(UNUSED_CHAT, message);

        verify(eventListener).auctionClosed();
    }

    @Test
    public void notifiesBidDetailsWhenCurrentPriceMessageReceived() {
        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else");

        translator.processMessage(UNUSED_CHAT, message);

        verify(eventListener).currentPrice(192, 7);
    }

}
