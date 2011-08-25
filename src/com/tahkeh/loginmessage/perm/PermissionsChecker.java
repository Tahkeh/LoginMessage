package com.tahkeh.loginmessage.perm;

import org.bukkit.entity.Player;

import com.nijiko.permissions.PermissionHandler;

public abstract class PermissionsChecker {

    public static class PermissionsPluginChecker extends PermissionsChecker {

        private final PermissionHandler handler;

        public PermissionsPluginChecker(PermissionHandler handler) {
            this.handler = handler;
        }

        @Override
        public boolean has(Player player, String perm) {
            return this.handler.has(player, perm);
        }
    }

    public static class InteralPermissionsChecker extends PermissionsChecker {

        @Override
        public boolean has(Player player, String perm) {
            return player.hasPermission(perm);
        }

    }

    public abstract boolean has(Player player, String perm);
}
