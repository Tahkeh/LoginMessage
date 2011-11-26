package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.DefaultVariables;
import com.tahkeh.loginmessage.methods.variables.KickVariables;

public class KickReasonMethod extends OriginalMethod {

	@Override
	protected String call(OfflinePlayer player, String event, DefaultVariables globalParameters) {
		if (globalParameters instanceof KickVariables) {
			return ((KickVariables) globalParameters).reason;
		} else {
			return null;
		}
	}

}
