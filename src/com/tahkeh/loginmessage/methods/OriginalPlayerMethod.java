package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.variables.DefaultVariables;

public abstract class OriginalPlayerMethod extends OriginalMethod {

	@Override
	protected final String call(OfflinePlayer player, String event, DefaultVariables globalParameters) {
		if (player instanceof Player) {
			return this.call((Player) player, event);
		} else {
			return null;
		}
	}

	protected abstract String call(Player player, String event);

}