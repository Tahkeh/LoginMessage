package com.tahkeh.loginmessage.matcher.causes;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.tahkeh.loginmessage.matcher.DefaultMatcher.SignedTextData;
import com.tahkeh.loginmessage.util.ClassResolvers;
import com.tahkeh.loginmessage.util.ClassResolvers.ClassResolver;

import de.xzise.XLogger;

public class EntityCause implements Cause {

	private final ClassResolver damagerClass;
	private final boolean positive;

	public EntityCause(final String damagerClass, final boolean positive, final XLogger logger) {
		this.damagerClass = ClassResolvers.parseClassName(damagerClass, logger, true);
		this.positive = positive;
	}

	public EntityCause(final String text, final XLogger logger) {
		final SignedTextData signedTextData = new SignedTextData(text);
		this.damagerClass = ClassResolvers.parseClassName(signedTextData.unsignedText, logger, true);
		this.positive = signedTextData.positive;
	}

	@Override
	public boolean isPositive() {
		return this.positive;
	}

	protected Entity getDamagerIfMatched(final EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			final Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
			if (damager != null && this.damagerClass.isSuperClass(damager.getClass())) {
				return damager;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public boolean match(EntityDamageEvent event) {
		return this.getDamagerIfMatched(event) != null;
	}
}
