package com.goos.sniper.sniper;

public interface AuctionEventListener {
    void auctionClosed();

    void currentPrice(int price, int increment);
}
