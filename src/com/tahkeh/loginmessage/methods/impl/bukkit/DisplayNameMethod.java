package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.OriginalPlayerMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

public class DisplayNameMethod extends OriginalPlayerMethod {

	public DisplayNameMethod() {
		super("dpnm");
	}

	@Override
	protected String call(Player p, BukkitVariables globalParameters) {
		return p.getDisplayName();
	}

}
