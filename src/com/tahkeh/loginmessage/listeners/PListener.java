package com.tahkeh.loginmessage.listeners;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.tahkeh.loginmessage.Message;

public class PListener extends PlayerListener {
	private final Message msg;
	
	public PListener(Message msg) {
		this.msg = msg;
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		msg.onPlayerJoin(event);
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		msg.onPlayerQuit(event);
	}
	
	public void onPlayerKick(PlayerKickEvent event) {
		msg.onPlayerKick(event);
	}
	
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		msg.onPlayerCommandPreprocess(event);
	}
}
