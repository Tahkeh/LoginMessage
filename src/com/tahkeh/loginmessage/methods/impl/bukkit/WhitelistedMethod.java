package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.preset.bukkit.BooleanMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public class WhitelistedMethod extends BooleanMethod {

	public WhitelistedMethod(Message message) {
		super(0, message, "white");
	}

	@Override
	protected Boolean getBoolean(final Parameter[] preValues, PlayerVariables globalParameters) {
		return globalParameters.offlinePlayer.isWhitelisted();
	}

}
