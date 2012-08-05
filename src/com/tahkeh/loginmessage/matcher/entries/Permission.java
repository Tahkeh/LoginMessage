package com.tahkeh.loginmessage.matcher.entries;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.LoginMessage;
import de.xzise.wrappers.permissions.BufferPermission;
import de.xzise.wrappers.permissions.PermissionsHandler;

public class Permission extends DefaultEntry {

	private final PermissionsHandler checker;
	private final LoginMessage plugin;

	public Permission(String permission, PermissionsHandler checker, LoginMessage plugin) {
		super(permission);
		this.checker = checker;
		this.plugin = plugin;
	}

	@Override
	public boolean match(OfflinePlayer player) {
		return player.isOnline() ? this.checker.permission(this.plugin.getServer().getPlayerExact(player.getName()), BufferPermission.create(this.signedTextData.unsignedText, false)) : false;
	}

}
