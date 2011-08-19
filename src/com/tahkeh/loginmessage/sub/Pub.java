package com.tahkeh.loginmessage.sub;

import org.bukkit.entity.Player;

/**
 * Entry representing the pub node (everybody). If the trigger is not null, it
 * acts like "-pub" and will return for the trigger as player in
 * {@link #match(Player)} false.
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
    public boolean match(Player player) {
        return !player.equals(this.trigger);
    }

}
