package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.preset.DoubleMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public class SaturationMethod extends DoubleMethod<BukkitVariables, PlayerVariables> {

	public SaturationMethod() {
		super("sat", PlayerVariables.class);
	}

	@Override
	public Double getValue(PlayerVariables globalParameters) {
		if (globalParameters.offlinePlayer instanceof Player) {
			return Double.valueOf(((Player) globalParameters.offlinePlayer).getSaturation());
		} else {
			return null;
		}
	}

}
