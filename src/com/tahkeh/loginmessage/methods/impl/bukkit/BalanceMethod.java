package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.methods.DoubleMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

import de.xzise.wrappers.economy.EconomyHandler;

public class BalanceMethod extends DoubleMethod<BukkitVariables> {

	private final EconomyHandler economy;

	public BalanceMethod(final EconomyHandler economy) {
		super("balance");
		this.economy = economy;
	}

	@Override
	public Double getValue(BukkitVariables globalParameters) {
		return this.economy.getBalance(globalParameters.offlinePlayer.getName());
	}

}
