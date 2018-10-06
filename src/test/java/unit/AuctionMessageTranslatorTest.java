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
    private static final String SNIPER_ID = "sniper";

    private final AuctionEventListener eventListener = mock(AuctionEventListener.class);
    private final AuctionMessageTranslator translator = new AuctionMessageTranslator(SNIPER_ID, eventListener);

    @Test
    public void notifiesAuctionClosedWhenCloseMessageReceived() {
        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: CLOSE;");

        translator.processMessage(UNUSED_CHAT, message);

        verify(eventListener).auctionClosed();
    }

    @Test
    public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder() {
        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else");

        translator.processMessage(UNUSED_CHAT, message);

        verify(eventListener).currentPrice(192, 7, AuctionEventListener.PriceSource.FromOtherBidder);
    }

    @Test
    public void notifiesBidDetailsWhenCurrentPRiceMessageReceivedFromSniper() {
        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: " + SNIPER_ID);

        translator.processMessage(UNUSED_CHAT, message);

        verify(eventListener).currentPrice(192, 7, AuctionEventListener.PriceSource.FromSniper);
    }
}
