package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.preset.bukkit.CasePlayerMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public class TimeMethod extends CasePlayerMethod {

	private final Message message;

	public TimeMethod(final Message message) {
		super(0, "time");
		this.message = message;
	}

	@Override
	protected String call(Player player, final Parameter[] preValues, PlayerVariables globalParameters) {
		return this.message.getTime(player.getWorld().getTime());
	}

}
