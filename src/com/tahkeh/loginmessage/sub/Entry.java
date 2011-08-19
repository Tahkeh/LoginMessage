package com.tahkeh.loginmessage.sub;

import org.bukkit.entity.Player;

public interface Entry //Concept credit goes to xZise. This is for whatever you might put as a receiver/trigger!
{
    boolean isPositive();
	  boolean match(Player player);
}