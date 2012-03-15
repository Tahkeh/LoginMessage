package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.parameter.types.LongParameterType;
import com.tahkeh.loginmessage.methods.preset.bukkit.OriginalPlayerMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public class TotalExperienceMethod extends OriginalPlayerMethod {

	public TotalExperienceMethod() {
		super("totalxp");
	}

	@Override
	protected LongParameterType call(Player player, PlayerVariables globalParameters) {
		return new LongParameterType(player.getTotalExperience());
	}

}
