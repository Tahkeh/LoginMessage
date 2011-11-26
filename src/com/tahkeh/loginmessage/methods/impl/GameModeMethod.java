package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.CasePlayerMethod;

public class GameModeMethod extends CasePlayerMethod {

	@Override
	protected String call(Player player, String event) {
		switch (player.getGameMode()) {
		case CREATIVE:
			return "Creative";
		case SURVIVAL:
			return "Survival";
		default:
			return null;
		}
	}

}
