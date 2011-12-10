package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Main;
import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.OriginalPlayerMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class IPMethod extends OriginalPlayerMethod {

	private final Message message;

	public IPMethod(Message message) {
		super("ip");
		this.message = message;
	}

	@Override
	protected String call(Player player, Variables globalParameters) {
		return this.message.isLocal(player) ? Main.getExternalIp().getHostAddress() : player.getAddress().getAddress().getHostAddress();
	}

}
