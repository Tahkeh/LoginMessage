package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.OriginalPlayerMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class CurrentExperienceMethod extends OriginalPlayerMethod {

	public CurrentExperienceMethod() {
		super("curxp");
	}

	@SuppressWarnings("deprecation")
	@Override
	protected String call(Player player, Variables globalParameters) {
		try {
			return Float.toString(player.getExp());
		} catch (NoSuchMethodError e) {
			return Integer.toString(player.getExperience());
		}
	}

}
