package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.wrappers.economy.EconomyHandler;

public class BalanceMethod extends OriginalMethod {

	private final EconomyHandler economy;

	public BalanceMethod(final EconomyHandler economy) {
		this.economy = economy;
	}

	@Override
	protected String call(OfflinePlayer player, String event, Variables globalParameters) {
		return Double.toString(this.economy.getBalance(player.getName()));
	}

}
