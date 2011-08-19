package com.tahkeh.loginmessage.sub;

import org.bukkit.entity.Player;

import com.nijiko.permissions.PermissionHandler;

public class Permission extends DefaultEntry {

    private final PermissionHandler handler;

    public Permission(String permission, PermissionHandler handler) {
        super(permission);
        this.handler = handler;
    }

    @Override
    public boolean match(Player player) {
        return this.handler.has(player, this.value);
    }

}
