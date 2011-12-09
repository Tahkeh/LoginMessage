package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.DoubleMethod;

public class ExhaustMethod extends DoubleMethod {

	public ExhaustMethod() {
		super("exhaust");
	}

	@Override
	public Double getValue(OfflinePlayer player, String event) {
		if (player instanceof Player) {
			return Double.valueOf(((Player) player).getExhaustion());
		} else {
			return null;
		}
	}

}
