package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.DefaultVariables;

public class SizeMethod extends OriginalMethod {

	@Override
	protected String call(OfflinePlayer player, String event, DefaultVariables globalParameters) {
		return Integer.toString(Bukkit.getServer().getOnlinePlayers().length);
	}

}
