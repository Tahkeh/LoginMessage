package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.parameter.types.DoubleParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.preset.bukkit.OriginalPlayerMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

import de.xzise.MinecraftUtil;

public class CurrentExperienceMethod extends OriginalPlayerMethod {

	public CurrentExperienceMethod() {
		super("curxp");
	}

	@Override
	protected ParameterType call(Player player, PlayerVariables globalParameters) {
		return new DoubleParameterType(player.getExp(), MinecraftUtil.MAX_TWO_DECIMALS_FORMAT);
	}

}
