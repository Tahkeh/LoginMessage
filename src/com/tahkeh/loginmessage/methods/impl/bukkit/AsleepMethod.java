package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.preset.bukkit.BooleanMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public class AsleepMethod extends BooleanMethod {

	public AsleepMethod(final Message message) {
		super(0, message, "asleep");
	}

	@Override
	protected Boolean getBoolean(final Parameter[] preValues, PlayerVariables globalParameters) {
		if (globalParameters.offlinePlayer instanceof Player) {
			return ((Player) globalParameters.offlinePlayer).isSleeping();
		} else {
			return null;
		}
	}

}
