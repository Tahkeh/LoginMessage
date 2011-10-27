package com.tahkeh.loginmessage.handlers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.tahkeh.loginmessage.entries.causes.Cause;
import com.tahkeh.loginmessage.entries.causes.EntityCause;
import com.tahkeh.loginmessage.entries.causes.OtherCause;
import com.tahkeh.loginmessage.store.MaterialTable;

public class DeathHandler {
	
	private final Player victim;
	private final MaterialTable table;
	private String killer = "?";
	private boolean killerIsPlayer = false;
	
	public DeathHandler(Player victim, MaterialTable table) {
		this.victim = victim;
		this.table = table;
	}
	
	public static boolean matchCauses(String causeString, Collection<Cause> causes) {
		boolean match = false;
		for (Cause cause : causes) {
			if (cause.match(causeString)) {
				if (!cause.isPositive()) {
					return false;
				} else {
					match = true;
				}
			}
		}
		return match;
	}
	
	public Set<Cause> getCauses() {
		Set<Cause> causes = new HashSet<Cause>();
		EntityDamageEvent event = victim.getLastDamageCause();
		DamageCause damageCause = event.getCause();
		String cause = damageCause.name().toLowerCase();
		if (damageCause == DamageCause.ENTITY_ATTACK || damageCause == DamageCause.ENTITY_EXPLOSION) {
			Entity attacker = ((EntityDamageByEntityEvent)event).getDamager();
			causes.add(new EntityCause(attacker.getClass().getSimpleName()));
			if (attacker instanceof Player) {
				killer = ((Player) attacker).getName();
				killerIsPlayer = true;
			} else {
				killer = attacker.toString().substring(5).toLowerCase();
			}
		} else {
			causes.add(new OtherCause(cause));
		}
		
		return causes;
	}
	
	public String getItem(ItemStack material) {
		int id = material.getTypeId();
		byte data = material.getData().getData();
		String item = table.getMaterialName(id, data);
		
		return item;
	}
	
	public String getKiller() {
		return killer;
	}
	
	public boolean isKillerPlayer() {
		return killerIsPlayer;
	}
}
