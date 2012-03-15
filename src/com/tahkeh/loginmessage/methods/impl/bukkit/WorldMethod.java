package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.bukkit.OriginalPlayerMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public class WorldMethod extends OriginalPlayerMethod {

	public WorldMethod() {
		super("world");
	}

	@Override
	protected StringParameterType call(Player player, PlayerVariables globalParameters) {
		return new StringParameterType(player.getWorld().getName());
	}

}
