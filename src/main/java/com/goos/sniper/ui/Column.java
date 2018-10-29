package com.goos.sniper.ui;

import com.goos.sniper.sniper.SniperSnapshot;

public enum Column {
    ITEM_IDENTIFIER {
        @Override
        public Object valueIn(SniperSnapshot sniperSnapshot) {
            return sniperSnapshot.getItemId();
        }
    },
    LAST_PRICE {
        @Override
        public Object valueIn(SniperSnapshot sniperSnapshot) {
            return sniperSnapshot.getLastPrice();
        }
    },
    LAST_BID {
        @Override
        public Object valueIn(SniperSnapshot sniperSnapshot) {
            return sniperSnapshot.getLastBid();
        }
    },
    SNIPER_STATUS {
        @Override
        public Object valueIn(SniperSnapshot sniperSnapshot) {
            return sniperSnapshot.getSniperState().status();
        }
    };

    public static Column at(int offset) {
        return values()[offset];
    }

    public abstract Object valueIn(SniperSnapshot sniperSnapshot);

}
