package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.OriginalPlayerMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class DisplayNameMethod extends OriginalPlayerMethod {

	public DisplayNameMethod() {
		super("dpnm");
	}

	@Override
	protected String call(Player p, Variables globalParameters) {
		return p.getDisplayName();
	}

}
