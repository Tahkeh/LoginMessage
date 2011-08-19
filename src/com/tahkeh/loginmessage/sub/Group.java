package com.tahkeh.loginmessage.sub;

import org.bukkit.entity.Player;

import com.nijiko.permissions.PermissionHandler;

/**
 * Handles a group as trigger/reciever.
 * 
 * @author Tahkeh
 * 
 */
public class Group extends DefaultEntry
{
    private final PermissionHandler handler;

    public Group(String group, PermissionHandler handler) {
        super(group);
        this.handler = handler;
    }

    public boolean match(Player player) {
        return this.handler.getGroup(player.getWorld().getName(), player.getName()).equalsIgnoreCase(this.value);
    }

}