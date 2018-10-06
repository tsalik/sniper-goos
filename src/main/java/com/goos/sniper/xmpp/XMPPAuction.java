package com.goos.sniper.xmpp;

import com.goos.sniper.Main;
import com.goos.sniper.sniper.Auction;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

public class XMPPAuction implements Auction {

    private final Chat chat;

    public XMPPAuction(Chat chat) {
        this.chat = chat;
    }

    @Override
    public void join() {
        sendMessage(Main.JOIN_COMMAND_FORMAT);
    }

    @Override
    public void bid(int amount) {
        sendMessage(String.format(Main.BID_COMMAND_FORMAT, amount));
    }

    private void sendMessage(String format) {
        try {
            chat.sendMessage(format);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

}
