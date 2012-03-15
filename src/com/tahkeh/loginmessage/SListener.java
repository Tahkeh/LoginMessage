package com.tahkeh.loginmessage;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

// Reacts on plugin en-/disable to update the handlers.
public class SListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginDisable(PluginDisableEvent event) {
		if (Main.getEconomy().unload(event.getPlugin())) {
			Main.getEconomy().load();
		}
		if (Main.getPermissions().unload(event.getPlugin())) {
			Main.getPermissions().load();
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginEnable(PluginEnableEvent event) {
		Main.getEconomy().load(event.getPlugin());
		Main.getPermissions().load(event.getPlugin());
	}
}