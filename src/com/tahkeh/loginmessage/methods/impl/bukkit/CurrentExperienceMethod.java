package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.OriginalPlayerMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

public class CurrentExperienceMethod extends OriginalPlayerMethod {

	public CurrentExperienceMethod() {
		super("curxp");
	}

	@SuppressWarnings("deprecation")
	@Override
	protected String call(Player player, BukkitVariables globalParameters) {
		try {
			return Float.toString(player.getExp());
		} catch (NoSuchMethodError e) {
			return Integer.toString(player.getExperience());
		}
	}

}
