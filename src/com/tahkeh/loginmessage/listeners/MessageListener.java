package com.tahkeh.loginmessage.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.tahkeh.loginmessage.Message;

public class MessageListener implements Listener {
	private final Message msg;

	public MessageListener(Message msg) {
		this.msg = msg;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDeath(EntityDeathEvent event) {
		if (!(event instanceof PlayerDeathEvent)) { return; }
		PlayerDeathEvent pde = (PlayerDeathEvent) event;
		
		msg.onPlayerDeath(pde);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		msg.onPlayerJoin(event);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		msg.onPlayerQuit(event);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerKick(PlayerKickEvent event) {
		msg.onPlayerKick(event);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		msg.onPlayerCommandPreprocess(event);
	}
}
