package com.tahkeh.loginmessage.matcher.entries;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.LoginMessage;

public class World extends DefaultEntry {

	private final LoginMessage plugin;

	public World(String text, LoginMessage plugin) {
		super(text);
		this.plugin = plugin;
	}

	@Override
	public boolean match(OfflinePlayer player) {
		return player.isOnline() ? this.plugin.getServer().getPlayerExact(player.getName()).getWorld().getName().equalsIgnoreCase(this.signedTextData.unsignedText) : false;
	}
}