package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.CasePlayerMethod;

public class TimeMethod extends CasePlayerMethod {

	private final Message message;

	public TimeMethod(final Message message) {
		this.message = message;
	}

	@Override
	protected String call(Player player, String event) {
		return this.message.getTime(player.getWorld().getTime());
	}

}
