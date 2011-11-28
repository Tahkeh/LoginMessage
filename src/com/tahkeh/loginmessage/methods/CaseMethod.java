package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.DefaultVariables;

public abstract class CaseMethod extends DefaultMethod {

	public CaseMethod() {
		super(true, 0, 1);
	}

	@Override
	public String call(OfflinePlayer player, String event, Parameter[] parameters, DefaultVariables globalParameters) {
		Boolean upper = null;
		if (parameters.length == 0) {
			upper = false;
		} else if (parameters.length == 1) {
			upper = DefaultMethod.parseAsBoolean(parameters[0].parse());
		}
		if (upper == null) {
			return null;
		} else {
			String result = this.call(player, event);
			if (!upper) {
				return result.toLowerCase();
			} else {
				return result;
			}
		}
	}

	protected abstract String call(OfflinePlayer player, String event);
}
