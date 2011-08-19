package com.tahkeh.loginmessage.sub;

import java.util.TimerTask;

import com.tahkeh.loginmessage.Message;

public class Cooldown extends TimerTask //This class acts like a TimerTask, but it uses a constructor so I can get vital variables to be used in run()
{
	private final Message msg;
	private final String cdstr;
	
	
	public Cooldown(Message msg, String cdstr)
	{
		this.msg = msg;
		this.cdstr = cdstr;
	}
	
	public void run()
	{
		msg.setCooldownValues(cdstr, true);
		msg.setCooldownWaiting(cdstr, false);
	}

}
