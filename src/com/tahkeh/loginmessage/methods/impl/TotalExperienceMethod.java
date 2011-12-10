package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.OriginalPlayerMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class TotalExperienceMethod extends OriginalPlayerMethod {

	public TotalExperienceMethod() {
		super("totalxp");
	}

	@Override
	protected String call(Player player, Variables globalParameters) {
		return Integer.toString(player.getTotalExperience());
	}

}
