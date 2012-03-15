package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.preset.bukkit.HalfStepedMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public class HealthMethod extends HalfStepedMethod {

	public HealthMethod() {
		super("health");
	}

	@Override
	protected Integer getInteger(PlayerVariables globalParameters) {
		if (globalParameters.offlinePlayer instanceof Player) {
			return ((Player) globalParameters.offlinePlayer).getHealth();
		} else {
			return null;
		}
	}

}
