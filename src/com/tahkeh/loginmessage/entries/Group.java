package com.tahkeh.loginmessage.entries;

import org.bukkit.entity.Player;

import de.xzise.wrappers.permissions.PermissionsHandler;

public class Group extends DefaultEntry {
	private final PermissionsHandler handler;

	public Group(String group, PermissionsHandler handler) {
		super(group);
		this.handler = handler;
	}

	public boolean match(Player player) {
		String[] groups = this.handler.getGroup(player.getWorld().getName(), player.getName());
		for (String group : groups) {
			if (group.equalsIgnoreCase(this.value)) {
				return true;
			}
		}
		return false;
	}

}