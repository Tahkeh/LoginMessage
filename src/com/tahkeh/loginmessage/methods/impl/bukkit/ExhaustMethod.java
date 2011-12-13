package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.DoubleMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

public class ExhaustMethod extends DoubleMethod<BukkitVariables> {

	public ExhaustMethod() {
		super("exhaust");
	}

	@Override
	public Double getValue(BukkitVariables globalParameters) {
		if (globalParameters.offlinePlayer instanceof Player) {
			return Double.valueOf(((Player) globalParameters.offlinePlayer).getExhaustion());
		} else {
			return null;
		}
	}

}
