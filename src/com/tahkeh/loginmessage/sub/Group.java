package com.tahkeh.loginmessage.sub;

import org.bukkit.entity.Player;

import com.nijiko.permissions.PermissionHandler;

/**
 * Handles a group as trigger/reciever.
 * 
 * @author Tahkeh
 * 
 */
public class Group implements Entry
{
    private final String group;
    private final boolean positive;
    private final PermissionHandler handler;

    public Group(boolean positive, String group, PermissionHandler handler) {
        this.group = group;
        this.positive = positive;
        this.handler = handler;
    }

    public boolean match(Player player) {
        return this.handler.getGroup(player.getWorld().getName(), player.getName()).equalsIgnoreCase(this.group);
    }

    @Override
    public boolean isPositive() {
        return this.positive;
    }

}