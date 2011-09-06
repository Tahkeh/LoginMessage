package com.tahkeh.loginmessage.entries;

import org.bukkit.entity.Player;

import de.xzise.wrappers.permissions.BufferPermission;
import de.xzise.wrappers.permissions.PermissionsHandler;

public class Permission extends DefaultEntry {

    private final PermissionsHandler checker;

    public Permission(String permission, PermissionsHandler checker) {
        super(permission);
        this.checker = checker;
    }

    @Override
    public boolean match(Player player) {
        return this.checker.permission(player, BufferPermission.create(this.value, false));
    }

}
