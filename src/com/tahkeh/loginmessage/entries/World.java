package com.tahkeh.loginmessage.entries;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.Main;

public class World extends DefaultEntry {
	
	private final Main plugin;

	public World(String text, Main plugin) {
		super(text);
		this.plugin = plugin;
	}

	@Override
	public boolean match(OfflinePlayer player) {
		return player.isOnline() ? plugin.getServer().getPlayerExact(player.getName()).getWorld().getName().equalsIgnoreCase(this.value) : false;
	}
}