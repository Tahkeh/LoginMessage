package com.tahkeh.loginmessage.entries;

import org.bukkit.entity.Player;

import com.nijiko.permissions.PermissionHandler;

public class Group extends DefaultEntry
{
    private final PermissionHandler handler;

    public Group(String group, PermissionHandler handler) {
        super(group);
        this.handler = handler;
    }

    public boolean match(Player player) {
        //TODO: Maybe use Permissions 3 calls
        return this.handler.getGroup(player.getWorld().getName(), player.getName()).equalsIgnoreCase(this.value);
    }

}