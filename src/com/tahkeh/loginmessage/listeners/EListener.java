package com.tahkeh.loginmessage.listeners;

import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.PlayerDeathEvent;
import com.tahkeh.loginmessage.Message;

public class EListener extends EntityListener {
	private final Message msg;
	
	public EListener(Message msg) {
		this.msg = msg;
	}
	
	public void onEntityDeath(EntityDeathEvent event) {
		if (!(event instanceof PlayerDeathEvent)) { return; }
		PlayerDeathEvent pde = (PlayerDeathEvent) event;
		
		msg.onPlayerDeath(pde);
	}
}
