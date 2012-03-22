package com.tahkeh.loginmessage.matcher.causes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent;

import com.tahkeh.loginmessage.util.ClassResolvers;
import com.tahkeh.loginmessage.util.ClassResolvers.ClassResolver;

import de.xzise.XLogger;

public class ProjectileCause extends EntityCause {

	private final ClassResolver shooterClass;

	public ProjectileCause(final String shooterClass, final String damagerClass, final boolean positive, final XLogger logger) {
		super(damagerClass, positive, logger);
		this.shooterClass = ClassResolvers.parseClassName(shooterClass, logger, true);
	}

	@Override
	public boolean match(EntityDamageEvent event) {
		final Entity damager = this.getDamagerIfMatched(event);
		if (damager instanceof Projectile) {
			Entity shooter = ((Projectile) damager).getShooter();
			return this.shooterClass.isSuperClass(shooter.getClass());
		} else {
			return false;
		}
	}
}
