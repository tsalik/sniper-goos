package com.goos.sniper.sniper

import com.goos.sniper.Main

sealed class SniperState {

    abstract fun status(): String

}

object Joining : SniperState() {
    override fun status() = Main.STATUS_JOINING
}

object Bidding : SniperState() {
    override fun status() = Main.STATUS_BIDDING;
}

object  Winning : SniperState() {
    override fun status() = Main.STATUS_WINNING
}

object Lost : SniperState() {
    override fun status() = Main.STATUS_LOST
}

object Won : SniperState() {
    override fun status() = Main.STATUS_WON
}