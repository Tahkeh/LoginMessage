package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.CasePlayerMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class TimeMethod extends CasePlayerMethod {

	private final Message message;

	public TimeMethod(final Message message) {
		super("time");
		this.message = message;
	}

	@Override
	protected String call(Player player, Variables globalParameters) {
		return this.message.getTime(player.getWorld().getTime());
	}

}
