package com.tahkeh.loginmessage.entries;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.Main;

import de.xzise.wrappers.permissions.PermissionsHandler;

public class Group extends DefaultEntry {
	private final PermissionsHandler handler;
	private final Main plugin;

	public Group(String group, PermissionsHandler handler, Main plugin) {
		super(group);
		this.handler = handler;
		this.plugin = plugin;
	}

	public boolean match(OfflinePlayer player) {
		if (player.isOnline()) {
			String[] groups = this.handler.getGroup(plugin.getServer().getPlayerExact(player.getName()).getWorld().getName(), player.getName());
			for (String group : groups) {
				if (group.equalsIgnoreCase(this.value)) {
					return true;
				}
			}
		}
		return false;
	}

}