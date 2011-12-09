package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.DoubleMethod;

public class SaturationMethod extends DoubleMethod {

	public SaturationMethod() {
		super("sat");
	}

	@Override
	public Double getValue(OfflinePlayer player, String event) {
		if (player instanceof Player) {
			return Double.valueOf(((Player) player).getSaturation());
		} else {
			return null;
		}
	}

}
