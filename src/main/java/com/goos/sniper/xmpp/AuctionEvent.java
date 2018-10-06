package com.goos.sniper.xmpp;

import com.goos.sniper.sniper.AuctionEventListener;

import java.util.HashMap;
import java.util.Map;

class AuctionEvent {

    private final Map<String, String> fields = new HashMap<>();

    String type() {
        return get("Event");
    }

    int currentPrice() {
        return getInt("CurrentPrice");
    }

    int increment() {
        return getInt("Increment");
    }

    AuctionEventListener.PriceSource isFrom(String sniperId) {
        return sniperId.equals(bidder())
                ? AuctionEventListener.PriceSource.FromSniper
                : AuctionEventListener.PriceSource.FromOtherBidder;
    }

    private String get(String fieldName) {
        return fields.get(fieldName);
    }

    private int getInt(String fieldName) {
        return Integer.parseInt(fields.get(fieldName));
    }

    private String bidder() {
        return get("Bidder");
    }

    static AuctionEvent from(String messageBody) {
        AuctionEvent event = new AuctionEvent();
        for (String field : fieldsIn(messageBody)) {
            event.addField(field);
        }
        return event;
    }

    private void addField(String field) {
        String[] pair = field.split(":");
        fields.put(pair[0].trim(), pair[1].trim());
    }

    private static String[] fieldsIn(String messageBody) {
        return messageBody.split(";");
    }

}
