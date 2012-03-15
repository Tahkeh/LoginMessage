package com.tahkeh.loginmessage.methods.preset.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.preset.CaseMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public abstract class CasePlayerMethod extends CaseMethod<BukkitVariables, PlayerVariables> {

	public CasePlayerMethod(final int preValueCount, final String defaultName) {
		super(preValueCount, defaultName, PlayerVariables.class);
	}

	@Override
	protected String call(Parameter[] preValues, PlayerVariables globalParameters) {
		if (globalParameters.offlinePlayer instanceof Player) {
			return this.call((Player) globalParameters.offlinePlayer, preValues, globalParameters);
		} else {
			return null;
		}
	}

	protected abstract String call(Player player, Parameter[] preValues, PlayerVariables globalParameters);
}
