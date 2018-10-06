package unit;

import com.goos.sniper.sniper.Auction;
import com.goos.sniper.sniper.AuctionSniper;
import com.goos.sniper.sniper.SniperListener;
import org.junit.Test;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AuctionSniperTest {

    private final Auction auction = mock(Auction.class);
    private final SniperListener sniperListener = mock(SniperListener.class);
    private final AuctionSniper sniper = new AuctionSniper(auction, sniperListener);

    @Test
    public void reportsLostWhenAuctionCloses() {

        sniper.auctionClosed();

        verify(sniperListener).sniperLost();
    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;

        sniper.currentPrice(price, increment);

        verify(auction).bid(price + increment);
        verify(sniperListener, atLeast(1)).sniperBidding();
    }

}
