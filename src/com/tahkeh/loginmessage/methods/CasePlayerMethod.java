package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public abstract class CasePlayerMethod extends CaseMethod {

	@Override
	protected String call(OfflinePlayer player, String event) {
		if (player instanceof Player) {
			return this.call((Player) player, event);
		} else {
			return null;
		}
	}

	protected abstract String call(Player player, String event);
}
