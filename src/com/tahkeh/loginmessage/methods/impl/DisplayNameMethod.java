package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.OriginalPlayerMethod;

public class DisplayNameMethod extends OriginalPlayerMethod {

	@Override
	protected String call(Player p, String event) {
		return p.getDisplayName();
	}

}
