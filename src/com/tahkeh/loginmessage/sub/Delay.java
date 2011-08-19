package com.tahkeh.loginmessage.sub;

import java.util.TimerTask;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Message;

public class Delay extends TimerTask // This class acts like a TimerTask, but it
                                     // uses a constructor so I can get vital
                                     // variables to be used in run()
{
    private final Message msg;
    private final String key;
    private final Player p;
    private final String event;
    private final String[] cdstrs;

    public Delay(Message msg, String key, Player p, String event, String[] cdstrs) {
        this.msg = msg;
        this.key = key;
        this.p = p;
        this.event = event;
        this.cdstrs = cdstrs;
    }

    public void run() {
        msg.sendMessage(p, key, event);
        for (String cdstr : this.cdstrs) {

            msg.setCooldownValues(cdstr, false);
        }
    }

}
