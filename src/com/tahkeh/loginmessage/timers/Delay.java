package com.tahkeh.loginmessage.timers;

import java.util.List;
import java.util.TimerTask;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.timers.Cooldown.CooldownTask;

//This class acts like a TimerTask, but it uses a constructor so I can get vital variables to be used in run()
public class Delay extends TimerTask {
	private final Message msg;
	private final String key;
	private final Player p;
	private final String event;
	private final List<Player> possibleReceivers;
	private final CooldownTask task;

	public Delay(Message msg, String key, Player p, String event, List<Player> possibleReceivers, CooldownTask task) {
		this.msg = msg;
		this.key = key;
		this.p = p;
		this.event = event;
		this.possibleReceivers = possibleReceivers;
		this.task = task;
	}

	public void run() {
		msg.sendMessage(p, possibleReceivers, key, event, task);
	}

}