package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.OriginalPlayerMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

public class TotalExperienceMethod extends OriginalPlayerMethod {

	public TotalExperienceMethod() {
		super("totalxp");
	}

	@Override
	protected String call(Player player, BukkitVariables globalParameters) {
		return Integer.toString(player.getTotalExperience());
	}

}
