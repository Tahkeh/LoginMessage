package com.tahkeh.loginmessage.timers;

import java.util.List;
import java.util.TimerTask;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.variables.Variables;
import com.tahkeh.loginmessage.timers.Cooldown.CooldownTask;

//This class acts like a TimerTask, but it uses a constructor so I can get vital variables to be used in run()
public class Delay extends TimerTask {
	private final Message msg;
	private final String[] lines;
	private final OfflinePlayer p;
	private final List<Player> receivers;
	private final CooldownTask task;
	private final Variables variables;

	public Delay(Message msg, String[] lines, OfflinePlayer p, List<Player> receivers, CooldownTask task, Variables variables) {
		this.msg = msg;
		this.lines = lines;
		this.p = p;
		this.receivers = receivers;
		this.task = task;
		this.variables = variables;
	}

	public void run() {
		msg.sendMessage(p, receivers, lines, task, variables);
	}

}