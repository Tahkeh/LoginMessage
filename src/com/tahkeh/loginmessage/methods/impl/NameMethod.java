package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class NameMethod extends OriginalMethod {

	@Override
	protected String call(OfflinePlayer p, String event, Variables globalParameters) {
		return p.getName();
	}

}
