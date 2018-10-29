package com.goos.sniper.sniper;

public interface SniperListener {

    void sniperLost();

    void sniperStateChanged(SniperSnapshot sniperSnapshot);

    void sniperWon();

}
