package com.tahkeh.loginmessage.timers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.entity.Player;

public class Cooldown {
	
	private final Map<String, CooldownTask> tasks = new HashMap<String, Cooldown.CooldownTask>();
	
	public static final class CooldownTask extends TimerTask {
		private final Cooldown cooldown;
		private final Collection<String> cdStrs;
		private final int duration;
		
		private CooldownTask(Collection<String> cdstrs, Cooldown cooldown, int duration) {
			this.cooldown = cooldown;
			this.cdStrs = cdstrs;
			this.duration = duration;
		}
		
		public void trigger() {
			new Timer().schedule(this, this.duration);
		}

		@Override
        public void run() {
	        for (String cdstr : this.cdStrs) {
	            this.cooldown.cooledDown(cdstr);
            }
        }
	}
	
	public Cooldown() {
		
	}
	
	public CooldownTask createTask(Collection<String> cdstrs, Cooldown cooldown, int duration) {
		CooldownTask task = new CooldownTask(cdstrs, cooldown, duration);
		for (String cdstr : cdstrs) {
	        this.tasks.put(cdstr, task);
        }
		return task;
	}
	
	public void cooledDown(String key) {
		this.tasks.remove(key);
	}
	
	public boolean isCooledDown(Player player, String messageName, String event) {
		return !this.tasks.containsKey(createKey(player, messageName, event));
	}
	
	public static String createKey(Player player, String messageName, String event) {
		return player.getName() + "." + messageName + "." + event;
	}
}
