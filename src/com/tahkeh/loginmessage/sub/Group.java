package com.tahkeh.loginmessage.sub;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.perm.GroupGetter;

/**
 * Handles a group as trigger/reciever.
 * 
 * @author Tahkeh
 * 
 */
public class Group extends DefaultEntry
{
    private final GroupGetter groupGetter;

    public Group(String group, GroupGetter groupGetter) {
        super(group);
        this.groupGetter = groupGetter;
    }

    public boolean match(Player player) {
        return this.groupGetter.getGroup(player).equalsIgnoreCase(this.value);
    }

}