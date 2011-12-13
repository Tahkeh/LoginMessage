package com.tahkeh.loginmessage.methods;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

public abstract class CasePlayerMethod extends CaseMethod<BukkitVariables> {

	public CasePlayerMethod(String defaultName) {
		super(defaultName);
	}

	@Override
	protected String call(BukkitVariables globalParameters) {
		if (globalParameters.offlinePlayer instanceof Player) {
			return this.call((Player) globalParameters.offlinePlayer, globalParameters);
		} else {
			return null;
		}
	}

	protected abstract String call(Player player, BukkitVariables globalParameters);
}
