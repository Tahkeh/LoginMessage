package com.tahkeh.loginmessage.matcher.deathcauses;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EntityDeathCause extends DefaultDeathCause {
	private Entity killer;

	public EntityDeathCause(String text, Entity killer) {
		super(text);
		this.killer = killer;
	}
	
	public EntityDeathCause(SignedTextData signedTextData, Entity killer) {
		super(signedTextData);
		this.killer = killer;
	}

	public boolean match(DamageCause cause) {
		if (killer == null) { return false; }
		String classString = "Craft" + this.signedTextData.unsignedText;
		String dir = "org.bukkit.craftbukkit.entity.";
		if (cause == DamageCause.PROJECTILE) {
			Projectile proj = (Projectile) killer;
			if (proj.getShooter() == null && this.signedTextData.unsignedText.toLowerCase().equals("projectile")) {
				return true;
			} else if (proj.getShooter() != null) {
				killer = proj.getShooter();
			} else {
				return false;
			}
		}
		try {
			Class<?> trigger = Class.forName(dir + classString);
			Class<?> causeClass = Class.forName(dir + killer.getClass().getSimpleName());
			return trigger.isAssignableFrom(causeClass);
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
