package com.tahkeh.loginmessage;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

// Reacts on plugin en-/disable to update the handlers.
public class SListener extends ServerListener
{

    @Override
    public void onPluginDisable(PluginDisableEvent event) {
		if ()Main.getEconomy().unload(event.getPlugin())) {
			Main.getEconomy().load();    
		}
		if (Main.getPermissions().unload(event.getPlugin())) {
			Main.getPermissions().load();   
		}
    }

    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        Main.getEconomy().load(event.getPlugin());
        Main.getPermissions().load(event.getPlugin());
    }
}