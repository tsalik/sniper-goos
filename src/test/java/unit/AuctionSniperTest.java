package unit;

import com.goos.sniper.sniper.Auction;
import com.goos.sniper.sniper.AuctionEventListener;
import com.goos.sniper.sniper.AuctionSniper;
import com.goos.sniper.sniper.SniperListener;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class AuctionSniperTest {

    private final Auction auction = mock(Auction.class);
    private final SniperListener sniperListener = mock(SniperListener.class);
    private final AuctionSniper sniper = new AuctionSniper(auction, sniperListener);

    @Test
    public void reportsLostWhenAuctionClosesImmediately() {

        sniper.auctionClosed();

        verify(sniperListener).sniperLost();
    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;

        sniper.currentPrice(price, increment, AuctionEventListener.PriceSource.FromOtherBidder);

        verify(auction).bid(price + increment);
        verify(sniperListener, atLeast(1)).sniperBidding();
    }

    @Test
    public void reportsIsWinningWhenCurrentPriceComesFromSniper() {

        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromSniper);

        verify(sniperListener, atLeast(1)).sniperWinning();
    }

    @Test
    public void reportsLostIfAuctionClosesWhenBidding() {
        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.auctionClosed();

        verify(sniperListener, atLeastOnce()).sniperLost();
    }

    @Test
    public void reportsWonIfAuctionClosesWhenWinning() {
        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromSniper);
        sniper.auctionClosed();

        verify(sniperListener, atLeastOnce()).sniperWon();
    }

}
