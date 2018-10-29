package unit;

import com.goos.sniper.sniper.*;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class AuctionSniperTest {

    private static final String ITEM_ID = "itemId";
    private final Auction auction = mock(Auction.class);
    private final SniperListener sniperListener = mock(SniperListener.class);
    private final AuctionSniper sniper = new AuctionSniper(ITEM_ID, auction, sniperListener);

    @Test
    public void reportsLostWhenAuctionClosesImmediately() {

        sniper.auctionClosed();

        verify(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, 0, 0, Lost.INSTANCE));
    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;

        sniper.currentPrice(price, increment, AuctionEventListener.PriceSource.FromOtherBidder);

        verify(auction).bid(price + increment);
        verify(sniperListener, atLeast(1)).sniperStateChanged(new SniperSnapshot(ITEM_ID, 1001, 1026, Bidding.INSTANCE));
    }

    @Test
    public void reportsIsWinningWhenCurrentPriceComesFromSniper() {

        sniper.currentPrice(123, 12, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromSniper);

        verify(sniperListener, atLeast(1)).sniperStateChanged(new SniperSnapshot(ITEM_ID, 123, 123, Winning.INSTANCE));
    }

    @Test
    public void reportsLostIfAuctionClosesWhenBidding() {
        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.auctionClosed();

        verify(sniperListener, atLeastOnce()).sniperStateChanged(new SniperSnapshot(ITEM_ID, 123, 168, Lost.INSTANCE));
    }

    @Test
    public void reportsWonIfAuctionClosesWhenWinning() {
        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromSniper);
        sniper.auctionClosed();

        verify(sniperListener, atLeastOnce()).sniperStateChanged(new SniperSnapshot(ITEM_ID,123, 123, Won.INSTANCE));
    }

}
