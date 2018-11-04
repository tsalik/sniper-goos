package com.goos.sniper.sniper;

public interface Auction {

    void join();

    void bid(int amount);

    void addAuctionEventListener(AuctionEventListener auctionEventListener);
}
