package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.FloatMethod;

public class ExhaustMethod extends FloatMethod {

	@Override
	public Float getValue(OfflinePlayer player, String event) {
		if (player instanceof Player) {
			return ((Player) player).getExhaustion();
		} else {
			return null;
		}
	}

}
