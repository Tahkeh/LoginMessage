package com.tahkeh.loginmessage.matcher.entries;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.LoginMessage;

import de.xzise.wrappers.permissions.PermissionsHandler;

public class Group extends DefaultEntry {
	private final PermissionsHandler handler;
	private final LoginMessage plugin;

	public Group(String group, PermissionsHandler handler, LoginMessage plugin) {
		super(group);
		this.handler = handler;
		this.plugin = plugin;
	}

	public Group(final SignedTextData signedTextData, PermissionsHandler handler, LoginMessage plugin) {
		super(signedTextData);
		this.handler = handler;
		this.plugin = plugin;
	}

	public boolean match(OfflinePlayer player) {
		if (player.isOnline()) {
			String[] groups = this.handler.getGroup(this.plugin.getServer().getPlayerExact(player.getName()).getWorld().getName(), player.getName());
			for (String group : groups) {
				if (group.equalsIgnoreCase(this.signedTextData.unsignedText)) {
					return true;
				}
			}
		}
		return false;
	}

}