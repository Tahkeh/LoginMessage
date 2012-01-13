package com.tahkeh.loginmessage.entries;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Entry representing the pub node (everybody). If the trigger is not null, it
 * acts like "-pub" and will return false, if the trigger is the player
 * parameter in {@link #match(Player)}.
 * 
 * @author Fabian Neundorf
 */
public class Pub implements Entry {

    private final Player trigger;

    public Pub(Player trigger) {
        this.trigger = trigger;
    }

    @Override
    public boolean isPositive() {
        return true;
    }

    @Override
    public boolean match(OfflinePlayer player) {
        return !player.equals(this.trigger);
    }

}
