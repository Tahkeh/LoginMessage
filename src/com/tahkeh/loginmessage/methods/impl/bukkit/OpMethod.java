package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.preset.bukkit.BooleanMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public class OpMethod extends BooleanMethod {

	public OpMethod(Message message) {
		super(0, message, "op");
	}

	@Override
	protected Boolean getBoolean(final Parameter[] preValues, final PlayerVariables globalParameters) {
		return globalParameters.offlinePlayer.isOp();
	}

}
