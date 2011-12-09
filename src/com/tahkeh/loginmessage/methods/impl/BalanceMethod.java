package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.DoubleMethod;

import de.xzise.wrappers.economy.EconomyHandler;

public class BalanceMethod extends DoubleMethod {

	private final EconomyHandler economy;

	public BalanceMethod(final EconomyHandler economy) {
		super("balance");
		this.economy = economy;
	}

	@Override
	public Double getValue(OfflinePlayer player, String event) {
		return this.economy.getBalance(player.getName());
	}

}
