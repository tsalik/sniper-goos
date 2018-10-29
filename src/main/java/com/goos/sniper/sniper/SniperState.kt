package com.goos.sniper.sniper

import com.goos.sniper.Main

sealed class SniperState {

    abstract fun status(): String
    open fun whenAuctionClosed(): SniperState = throw IllegalStateException("Auction already closed")

}

object Joining : SniperState() {
    override fun status() = Main.STATUS_JOINING
    override fun whenAuctionClosed() = Lost
}

object Bidding : SniperState() {
    override fun status() = Main.STATUS_BIDDING
    override fun whenAuctionClosed() = Lost
}

object  Winning : SniperState() {
    override fun status() = Main.STATUS_WINNING
    override fun whenAuctionClosed() = Won
}

object Lost : SniperState() {
    override fun status() = Main.STATUS_LOST
}

object Won : SniperState() {
    override fun status() = Main.STATUS_WON
}