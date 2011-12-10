package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class IfSetMethod extends IfMethod {

	public IfSetMethod(final boolean inverted) {
		super(1, inverted);
	}

	@Override
	protected Boolean match(OfflinePlayer player, Parameter[] preValues, Variables globalParameters) {
		return preValues[0].parse() != null;
	}
}
