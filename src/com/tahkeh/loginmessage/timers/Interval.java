package com.tahkeh.loginmessage.timers;

import com.tahkeh.loginmessage.Message;

public class Interval implements Runnable {
	private final Message msg;
	private final String key;
	
	public Interval(Message msg, String key) {
		this.msg = msg;
		this.key = key;
	}
	
	public void run() {
		msg.onIntervalCompletion(key);
	}

}
