package com.tahkeh.loginmessage.methods;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

public abstract class OriginalPlayerMethod extends OriginalMethod<BukkitVariables> {

	public OriginalPlayerMethod(final String defaultName) {
		super(defaultName);
	}

	@Override
	protected final String call(BukkitVariables globalParameters) {
		if (globalParameters.offlinePlayer instanceof Player) {
			return this.call((Player) globalParameters.offlinePlayer, globalParameters);
		} else {
			return null;
		}
	}

	protected abstract String call(Player player, BukkitVariables globalParameters);

}
