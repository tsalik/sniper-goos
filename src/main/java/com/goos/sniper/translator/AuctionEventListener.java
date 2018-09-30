package com.goos.sniper.translator;

public interface AuctionEventListener {
    void auctionClosed();

    void currentPrice(int price, int increment);
}
