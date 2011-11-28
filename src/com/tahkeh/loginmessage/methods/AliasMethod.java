package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.DefaultVariables;

public class AliasMethod extends DefaultMethod {

	private final String result;

	public AliasMethod(final String result, final int paramCount) {
		super(true, paramCount);
		this.result = result;
	}

	@Override
	public String call(OfflinePlayer player, String event, Parameter[] parameters, DefaultVariables globalParameters) {
		if (this.getParamCounts()[0] == parameters.length) {
			String result = this.result;
			for (int i = 0; i < parameters.length; i++) {
				result = result.replaceAll("\\$" + i + ";", parameters[i].parse());
			}
			return result;
		} else {
			return null;
		}
	}

}
