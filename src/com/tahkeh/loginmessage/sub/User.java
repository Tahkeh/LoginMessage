package com.tahkeh.loginmessage.sub;

import org.bukkit.entity.Player;

public class User implements Entry // For normal Players
{
    public final String user;
    private final boolean positive;

    public User(boolean positive, String user) {
        this.positive = positive;
        this.user = user;
    }

    public boolean match(Player player) {
        return player.getName().equals(user);
    }

    @Override
    public boolean isPositive() {
        return this.positive;
    }
}