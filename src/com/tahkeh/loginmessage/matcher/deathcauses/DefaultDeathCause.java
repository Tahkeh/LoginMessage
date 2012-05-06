package com.tahkeh.loginmessage.matcher.deathcauses;

import org.bukkit.event.entity.EntityDamageEvent;

import com.tahkeh.loginmessage.matcher.DefaultMatcher;

public abstract class DefaultDeathCause extends DefaultMatcher<EntityDamageEvent.DamageCause> implements DeathCause {
	
	protected DefaultDeathCause(final String text) {
		super(text);
	}

	protected DefaultDeathCause(final SignedTextData signedTextData) {
		super(signedTextData);
	}
}
