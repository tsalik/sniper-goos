@file:JvmName("SniperSnapshotUtils")
package com.goos.sniper.sniper

data class SniperSnapshot(val itemId: String,
                          val lastPrice: Int,
                          val lastBid: Int,
                          val sniperState: SniperState) {

    fun bidding(price: Int, bid: Int) = SniperSnapshot(itemId, price, bid, Bidding)

    fun winning(price: Int) = SniperSnapshot(itemId, price, price, Winning)

}

fun joining(itemId: String) = SniperSnapshot(itemId, 0, 0, Joining)