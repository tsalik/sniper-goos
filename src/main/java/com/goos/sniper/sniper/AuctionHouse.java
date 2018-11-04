package com.goos.sniper.sniper;

public interface AuctionHouse {

    Auction auctionFor(String itemId);

    void disconnect();

}
