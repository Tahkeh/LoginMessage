package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.CasePlayerMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class AsleepMethod extends CasePlayerMethod {

	private final Message message;
	
	public AsleepMethod(final Message message) {
		super("asleep");
		this.message = message;
	}

	@Override
	protected String call(Player player, Variables globalParameters) {
		return this.message.booleanToName(player.isSleeping());
	}

}
