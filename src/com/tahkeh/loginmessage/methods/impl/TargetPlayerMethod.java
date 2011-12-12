package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.PlayerNotFoundVariables;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class TargetPlayerMethod extends OriginalMethod {

	public TargetPlayerMethod() {
		super("target");
	}

	@Override
	protected String call(OfflinePlayer player, Variables globalParameters) {
		if (globalParameters instanceof PlayerNotFoundVariables) {
			return ((PlayerNotFoundVariables) globalParameters).targetName;
		} else {
			return null;
		}
	}

}
