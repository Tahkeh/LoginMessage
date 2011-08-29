package com.tahkeh.loginmessage.entries;

import org.bukkit.entity.Player;

/**
 * Basic entry files. Simply checks if the player is matched by this player. Any
 * entry could be positive or negative. If the entry is matching the player and
 * it is positive, than the player is meant by this entry. If the entry is
 * matching and negative, than it wasn't meant by this entry and should skipped
 * (no other positive node override this negative one).
 * 
 * @author Tahkeh
 */
public interface Entry {
    /**
     * If the entry is positive, matching players are meant. If the entry is
     * negative the matching players are never meant.
     * 
     * @return If this entry is positive.
     */
    boolean isPositive();

    /**
     * Returns if the player matches the entry.
     * 
     * @param player
     *            the player to test.
     * @return if the player matches the entry.
     */
    boolean match(Player player);
}