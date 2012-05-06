package com.tahkeh.loginmessage.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.tahkeh.loginmessage.matcher.deathcauses.DeathCause;
import com.tahkeh.loginmessage.matcher.deathcauses.EntityDeathCause;
import com.tahkeh.loginmessage.matcher.deathcauses.NonEntityDeathCause;
import com.tahkeh.loginmessage.store.MaterialTable;

public class DeathHandler {
	
	private final Player victim;
	private final MaterialTable table;
	private Entity killer = null;
	private String killerName = "?";
	
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
	
	public boolean matchCauses(List<String> triggerCauses) {
		Set<DeathCause> causes = getCauses(triggerCauses);
		DamageCause damageCause = victim.getLastDamageCause().getCause();
		boolean match = false;
		for (DeathCause cause : causes) {
			if (cause.match(damageCause)) {
				if (!cause.isPositive()) {
					return false;
				} else {
					match = true;
				}
			}
		}
		return match;
	}
	
	public Set<DeathCause> getCauses(List<String> triggerCauses) {
		List<String> entityCauses = new ArrayList<String>();
		Collections.addAll(entityCauses, "projectile", "entity_attack", "entity_explode");
		Set<DeathCause> causes = new HashSet<DeathCause>();
		for (String cause : triggerCauses) {
			if (inLava()) {
				cause = "lava";
			}
			DamageCause damageCause = null;
			try {
				damageCause = DamageCause.valueOf(cause.toUpperCase());
			} catch (IllegalArgumentException e) {
			}
			if ((damageCause != null) && !entityCauses.contains(cause.toLowerCase())) {
				if (cause.toLowerCase().equals("suffocation")) {
					killerName = getSuffocator();
				}
				causes.add(new NonEntityDeathCause(cause));
			} else {
				Entity attacker = null;
				try {
					attacker = ((EntityDamageByEntityEvent)victim.getLastDamageCause()).getDamager();
				} catch (ClassCastException e) {
				}
				if (attacker != null) {
					killer = attacker;
					if (killer instanceof Projectile) {
						Projectile proj = (Projectile) killer;
						killer = proj.getShooter();
					}
					killerName = killerIsPlayer() ? ((Player)killer).getName() : killer.getType().getName().toLowerCase();
					killer = attacker;
				}
				causes.add(new EntityDeathCause(cause, killer));
			}
		}
		return causes;
	}
	
	public String getItem(ItemStack material) {
		int id = material.getTypeId();
		byte data = material.getData().getData();
		String item = table.getMaterialName(id, data);
		
		return item;
	}
	
	public Entity getKiller() {
		return killer;
	}
	
	public String getKillerName() {
		return killerName;
	}
	
	public boolean killerIsPlayer() {
		return killer instanceof Player;
	}
}
