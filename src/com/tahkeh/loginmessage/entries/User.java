package com.tahkeh.loginmessage.entries;

import org.bukkit.entity.Player;

public class User extends DefaultEntry {

    public User(String user) {
        super(user);
    }

    public boolean match(Player player) {
        return player.getName().equals(this.value);
    }
}