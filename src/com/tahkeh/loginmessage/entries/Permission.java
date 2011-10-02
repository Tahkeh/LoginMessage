package com.tahkeh.loginmessage.entries;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.Main;

import de.xzise.wrappers.permissions.BufferPermission;
import de.xzise.wrappers.permissions.PermissionsHandler;

public class Permission extends DefaultEntry {

    private final PermissionsHandler checker;
    private final Main plugin;

    public Permission(String permission, PermissionsHandler checker, Main plugin) {
        super(permission);
        this.checker = checker;
        this.plugin = plugin;
    }

    @Override
    public boolean match(OfflinePlayer player) {
        return player.isOnline() ? this.checker.permission(plugin.getServer().getPlayerExact(player.getName()), BufferPermission.create(this.value, false)) : false;
    }

}
