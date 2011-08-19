package com.tahkeh.loginmessage.sub;

import java.util.TimerTask;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Message;

public class Delay extends TimerTask //This class acts like a TimerTask, but it uses a constructor so I can get vital variables to be used in run()
{
	private final Message msg;
	private final String key;
	private final Player all;
	private final Player p;
	private final String event;
	public Delay(Message msg, String key, Player all, Player p, String event)
	{
		this.msg = msg;
		this.key = key;
		this.all = all;
		this.p = p;
		this.event = event;
	}
	
	public void run()
	{
		String cdstr = all.getName() + "." + event + "." + key;
		msg.sendMessage(all, p, key, event);
		msg.setCooldownValues(cdstr, false);
	}

}
