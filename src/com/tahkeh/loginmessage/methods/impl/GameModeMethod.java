package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.CasePlayerMethod;

public class GameModeMethod extends CasePlayerMethod {

	private final Message message;

	public GameModeMethod(final Message message) {
		super("mode");
		this.message = message;
	}

	@Override
	protected String call(Player player, String event) {
		return this.message.getGameModeText(player.getGameMode());
	}

}
