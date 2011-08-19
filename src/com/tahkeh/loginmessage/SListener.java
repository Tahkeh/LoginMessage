package com.tahkeh.loginmessage;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

public class SListener extends ServerListener //Etc, etc, for iConomy
{
    private Main plugin;

    public SListener(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginDisable(PluginDisableEvent event) {
        if (Main.iConomy != null) {
            if (event.getPlugin().getDescription().getName().equals("iConomy")) {
                Main.Permissions = null;
            }
        }
    }

    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        if (Main.iConomy == null) {
            Plugin iConomy = plugin.getServer().getPluginManager().getPlugin("iConomy");

            if (iConomy != null && plugin.iConomyEnabled() == true) {
                if (iConomy.isEnabled()) {
                    Main.iConomy = (com.iConomy.iConomy) iConomy;
                }
            }
        }
    }
}