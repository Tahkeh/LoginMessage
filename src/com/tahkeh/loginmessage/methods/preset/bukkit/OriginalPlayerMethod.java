package com.tahkeh.loginmessage.methods.preset.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.preset.OriginalCastedMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public abstract class OriginalPlayerMethod extends OriginalCastedMethod<BukkitVariables, PlayerVariables> {

	public OriginalPlayerMethod(final String defaultName) {
		super(defaultName, PlayerVariables.class);
	}

	@Override
	protected final ParameterType call(PlayerVariables globalParameters) {
		if (globalParameters.offlinePlayer instanceof Player) {
			return this.call((Player) globalParameters.offlinePlayer, globalParameters);
		} else {
			return null;
		}
	}

	protected abstract ParameterType call(Player player, PlayerVariables globalParameters);

}
