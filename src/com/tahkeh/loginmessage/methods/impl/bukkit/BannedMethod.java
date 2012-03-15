package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.preset.bukkit.BooleanMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public class BannedMethod extends BooleanMethod {

	public BannedMethod(Message message) {
		super(0, message, "banned");
	}

	@Override
	protected Boolean getBoolean(final Parameter[] preValues, final PlayerVariables globalParameters) {
		return globalParameters.offlinePlayer.isBanned();
	}

}
