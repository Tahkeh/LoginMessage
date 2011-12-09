package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.variables.Variables;

public abstract class OriginalPlayerMethod extends OriginalMethod {

	public OriginalPlayerMethod(final String defaultName) {
		super(defaultName);
	}

	@Override
	protected final String call(OfflinePlayer player, String event, Variables globalParameters) {
		if (player instanceof Player) {
			return this.call((Player) player, event);
		} else {
			return null;
		}
	}

	protected abstract String call(Player player, String event);

}
