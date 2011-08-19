package com.tahkeh.loginmessage.sub;

import org.bukkit.entity.Player;

public class User implements Entry //For normal Players
{
	  public final String user;

	  public User(String user){
		  this.user = user;
	  }

	public boolean match(Player player) {
	    return player.getName().equals(user);
	  }
	public boolean match(String player)
	{
	  return player.equals(user);
	}
	}