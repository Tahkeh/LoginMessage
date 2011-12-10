package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.DoubleMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class ExhaustMethod extends DoubleMethod {

	public ExhaustMethod() {
		super("exhaust");
	}

	@Override
	public Double getValue(OfflinePlayer player, Variables globalParameters) {
		if (player instanceof Player) {
			return Double.valueOf(((Player) player).getExhaustion());
		} else {
			return null;
		}
	}

}
