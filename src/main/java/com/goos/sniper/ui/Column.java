package com.goos.sniper.ui;

import com.goos.sniper.sniper.SniperSnapshot;

public enum Column {
    ITEM_IDENTIFIER("Item") {
        @Override
        public Object valueIn(SniperSnapshot sniperSnapshot) {
            return sniperSnapshot.getItemId();
        }
    },
    LAST_PRICE("Last Price") {
        @Override
        public Object valueIn(SniperSnapshot sniperSnapshot) {
            return sniperSnapshot.getLastPrice();
        }
    },
    LAST_BID("Last Bid") {
        @Override
        public Object valueIn(SniperSnapshot sniperSnapshot) {
            return sniperSnapshot.getLastBid();
        }
    },
    SNIPER_STATUS("State") {
        @Override
        public Object valueIn(SniperSnapshot sniperSnapshot) {
            return sniperSnapshot.getSniperState().status();
        }
    };

    public final String name;

    public static Column at(int offset) {
        return values()[offset];
    }

    public abstract Object valueIn(SniperSnapshot sniperSnapshot);

    Column(String name) {
        this.name = name;
    }

}
