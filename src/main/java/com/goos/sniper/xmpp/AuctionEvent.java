package com.goos.sniper.xmpp;

import java.util.HashMap;
import java.util.Map;

class AuctionEvent {

    private final Map<String, String> fields = new HashMap<>();

    public String type() {
        return get("Event");
    }

    public int currentPrice() {
        return getInt("CurrentPrice");
    }

    public int increment() {
        return getInt("Increment");
    }

    private String get(String fieldName) {
        return fields.get(fieldName);
    }

    private int getInt(String fieldName) {
        return Integer.parseInt(fields.get(fieldName));
    }

    static AuctionEvent from(String messageBody) {
        AuctionEvent event = new AuctionEvent();
        for (String field: fieldsIn(messageBody)) {
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
