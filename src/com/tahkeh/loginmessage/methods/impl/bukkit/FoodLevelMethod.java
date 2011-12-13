package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.DefaultMethod;
import com.tahkeh.loginmessage.methods.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

public class FoodLevelMethod extends DefaultNamedMethod<BukkitVariables> {

	public FoodLevelMethod() {
		super(true, "food", 0, 1);
	}

	@Override
	public String call(Parameter[] parameters, BukkitVariables globalParameters) {
		if (globalParameters.offlinePlayer instanceof Player) {
			final Boolean halfsteps;
			if (parameters.length == 0) {
				halfsteps = true;
			} else if (parameters.length == 1) {
				halfsteps = DefaultMethod.parseAsBoolean(parameters[0].parse());
			} else {
				halfsteps = null;
			}
			if (halfsteps != null) {
				final int health = ((Player) globalParameters.offlinePlayer).getFoodLevel();
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
