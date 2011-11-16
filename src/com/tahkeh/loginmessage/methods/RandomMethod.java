package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;

import de.xzise.MinecraftUtil;

public class RandomMethod implements Method {

	@Override
	public String call(OfflinePlayer player, String event, String... parameters) {
		return MinecraftUtil.getRandom(parameters);
	}

	@Override
	public boolean recursive() {
		return true;
	}

}
