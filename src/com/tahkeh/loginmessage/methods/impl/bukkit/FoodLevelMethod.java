package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.preset.bukkit.HalfStepedMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public class FoodLevelMethod extends HalfStepedMethod {

	public FoodLevelMethod() {
		super("food");
	}

	@Override
	protected Integer getInteger(PlayerVariables globalParameters) {
		if (globalParameters.offlinePlayer instanceof Player) {
			return ((Player) globalParameters.offlinePlayer).getFoodLevel();
		} else {
			return null;
		}
	}

}
