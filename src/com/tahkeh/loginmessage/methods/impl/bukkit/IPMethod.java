package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.bukkit.OriginalPlayerMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public class IPMethod extends OriginalPlayerMethod {

	private final Message message;

	public IPMethod(Message message) {
		super("ip");
		this.message = message;
	}

	@Override
	protected StringParameterType call(Player player, PlayerVariables globalParameters) {
		return new StringParameterType(this.message.getIP(player));
	}

}
