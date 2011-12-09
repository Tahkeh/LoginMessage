package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.DefaultMethod;
import com.tahkeh.loginmessage.methods.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class HealthMethod extends DefaultNamedMethod {

	public HealthMethod() {
		super(true, "health", 0, 1);
	}

	@Override
	public String call(OfflinePlayer player, String event, Parameter[] parameters, Variables globalParameters) {
		if (player instanceof Player) {
			final Boolean halfsteps;
			if (parameters.length == 0) {
				halfsteps = true;
			} else if (parameters.length == 1) {
				halfsteps = DefaultMethod.parseAsBoolean(parameters[0].parse());
			} else {
				halfsteps = null;
			}
			if (halfsteps != null) {
				final int health = ((Player) player).getHealth();
				if (halfsteps) {
					return Double.toString(((double) health) / 2);
				} else {
					return Integer.toString(health);
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
