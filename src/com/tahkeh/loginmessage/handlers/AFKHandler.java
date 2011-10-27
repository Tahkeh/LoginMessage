package com.tahkeh.loginmessage.handlers;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Main;

import belgium.Balor.Workers.AFKWorker;

public class AFKHandler {
	private final Main plugin;

	public AFKHandler(Main plugin) {
		this.plugin = plugin;
	}

	public boolean isActive() {
		return plugin.getServer().getPluginManager().isPluginEnabled("AdminCmd");
	}

	public boolean isAFK(Player p) {
		return AFKWorker.getInstance().isAfk(p);
	}
}
