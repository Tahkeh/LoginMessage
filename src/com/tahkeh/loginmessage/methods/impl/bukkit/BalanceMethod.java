package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.methods.preset.DoubleMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

import de.xzise.wrappers.economy.EconomyHandler;

public class BalanceMethod extends DoubleMethod<BukkitVariables, PlayerVariables> {

	private final EconomyHandler economy;

	public BalanceMethod(final EconomyHandler economy) {
		super("balance", PlayerVariables.class);
		this.economy = economy;
	}

	@Override
	public Double getValue(PlayerVariables globalParameters) {
		return this.economy.getBalance(globalParameters.offlinePlayer.getName());
	}

}
