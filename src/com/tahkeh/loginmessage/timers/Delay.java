package com.tahkeh.loginmessage.timers;

import java.util.List;
import java.util.TimerTask;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.timers.Cooldown.CooldownTask;

//This class acts like a TimerTask, but it uses a constructor so I can get vital variables to be used in run()
public class Delay extends TimerTask {
	private final Message msg;
	private final String[] lines;
	private final Player p;
	private final String event;
	private final List<Player> receivers;
	private final CooldownTask task;

	public Delay(Message msg, String[] lines, Player player, String event, List<Player> receivers, CooldownTask task) {
		this.msg = msg;
		this.lines = lines;
		this.p = player;
		this.event = event;
		this.receivers = receivers;
		this.task = task;
	}

	public void run() {
		msg.sendMessage(p, receivers, lines, event, task);
	}

}