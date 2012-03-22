package com.tahkeh.loginmessage.handlers;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.tahkeh.loginmessage.Message;
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
	
	private boolean inLava() {
		return victim.getWorld().getBlockAt(victim.getLocation()).getType() == Material.LAVA;
	}
	
	private String getSuffocator() {
		Block b = victim.getWorld().getBlockAt(victim.getLocation());
		return table.getMaterialName(b.getType().getId(), b.getData());
	}
	
	public static boolean matchCauses(List<String> possibleCauses, Collection<Cause> causes) {
		boolean match = false;
		for (String causeString : possibleCauses) {
			boolean negative = causeString.charAt(0) == '-';
			if (negative) {
				causeString = causeString.substring(1);
			}
			for (Cause cause : causes) {
				if (cause.match(Message.toCapitalCase(causeString))) {
					if (negative) {
						match = false;
						break;
					} else {
						match = true;
					}
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
			String attackerClass = attacker.getClass().getSimpleName();
			
			//Special cases for mobs with projectiles
			if (attackerClass.equals("CraftProjectile")) {
				attackerClass = "CraftSkeleton";
			}
			if (attackerClass.equals("CraftFireball")) {
				attackerClass = "CraftGhast";
			}
			if (attackerClass.equals("CraftSmallFireball")) {
				attackerClass = "CraftBlaze";
			}
			causes.add(new EntityCause(attackerClass));
			if (attacker instanceof Player) {
				killer = ((Player) attacker).getName();
				killerIsPlayer = true;
			} else {
				killer = attacker.toString().substring(5).toLowerCase();
			}
		} else {
			if (inLava()) {
				cause = "lava";
			}
			causes.add(new OtherCause(cause));
		}
		if (damageCause == DamageCause.PROJECTILE) {
			killer = ((EntityDamageByEntityEvent)event).getDamager().toString().substring(5).toLowerCase();
		}
		if (damageCause == DamageCause.SUFFOCATION) {
			killer = getSuffocator();
		}
		return causes;
	}
	
	public String getItem(ItemStack material) {
		return this.table.getMaterialName(material.getData());
	}
	
	public String getKiller() {
		return killer;
	}
	
	public boolean isKillerPlayer() {
		return killerIsPlayer;
	}
}
