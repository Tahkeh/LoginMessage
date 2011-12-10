package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class SizeMethod extends OriginalMethod {

	public SizeMethod() {
		super("size");
	}

	@Override
	protected String call(OfflinePlayer player, Variables globalParameters) {
		return Integer.toString(Bukkit.getServer().getOnlinePlayers().length);
	}

}
