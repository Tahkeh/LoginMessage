package com.tahkeh.loginmessage.sub;

import org.bukkit.entity.Player;

	  public class Group implements Entry //For Permissions groups
	  {
	  public final String group;
	  public final String handler;

	  public Group(String group, String handler){
		  this.group = group;
		  this.handler = handler;
	  }

	  public boolean match(Player player) {
		  return handler.equalsIgnoreCase(group);
	}
	  public boolean match(String string)
	  {
	    return handler.equalsIgnoreCase(group);
	  }

}