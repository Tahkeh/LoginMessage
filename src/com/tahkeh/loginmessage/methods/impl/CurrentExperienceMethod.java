package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.OriginalPlayerMethod;

public class CurrentExperienceMethod extends OriginalPlayerMethod {

	@Override
	protected String call(Player player, String event) {
		return Integer.toString(player.getExperience());
	}

}
