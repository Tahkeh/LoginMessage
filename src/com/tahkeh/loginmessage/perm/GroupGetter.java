package com.tahkeh.loginmessage.perm;

import org.bukkit.entity.Player;

import com.nijiko.permissions.PermissionHandler;

public abstract class GroupGetter {

    public static class PermsGroupGetter extends GroupGetter {

        private final PermissionHandler handler;

        public PermsGroupGetter(PermissionHandler handler) {
            this.handler = handler;
        }

        @Override
        public String getGroup(Player player) {
            return this.handler.getGroup(player.getWorld().getName(), player.getName());
        }

    }

    public abstract String getGroup(Player player);

}
