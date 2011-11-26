package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.DeathVariables;
import com.tahkeh.loginmessage.methods.variables.DefaultVariables;

public class DeathEntityMethod extends OriginalMethod {

	@Override
	protected String call(OfflinePlayer player, String event, DefaultVariables globalParameters) {
		if (globalParameters instanceof DeathVariables) {
			return ((DeathVariables) globalParameters).entity;
		} else {
			return null;
		}
	}

}
