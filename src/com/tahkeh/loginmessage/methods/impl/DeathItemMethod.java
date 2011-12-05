package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.DeathVariables;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class DeathItemMethod extends OriginalMethod {

	@Override
	protected String call(OfflinePlayer player, String event, Variables globalParameters) {
		if (globalParameters instanceof DeathVariables) {
			return ((DeathVariables) globalParameters).item;
		} else {
			return null;
		}
	}
}
