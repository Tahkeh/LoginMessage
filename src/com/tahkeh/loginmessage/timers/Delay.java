package com.tahkeh.loginmessage.timers;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.timers.Cooldown.CooldownTask;

//This class acts like a TimerTask, but it uses a constructor so I can get vital variables to be used in run()
public class Delay extends TimerTask {
	private final Message msg;
	private final String key;
	private final OfflinePlayer p;
	private final String event;
	private final List<Player> possibleReceivers;
	private final CooldownTask task;
	private final Map<String, String> args;

	public Delay(Message msg, String key, OfflinePlayer p, String event, List<Player> possibleReceivers, CooldownTask task, Map<String, String> args) {
		this.msg = msg;
		this.key = key;
		this.p = p;
		this.event = event;
		this.possibleReceivers = possibleReceivers;
		this.task = task;
		this.args = args;
	}

	public void run() {
		msg.sendMessage(p, possibleReceivers, key, event, task, args);
	}

}