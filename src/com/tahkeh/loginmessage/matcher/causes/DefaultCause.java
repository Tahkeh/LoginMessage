package com.tahkeh.loginmessage.matcher.causes;

import org.bukkit.event.entity.EntityDamageEvent;

import com.tahkeh.loginmessage.matcher.DefaultMatcher;

// Generation 2 Default Cause
public abstract class DefaultCause extends DefaultMatcher<EntityDamageEvent> implements Cause {

	protected DefaultCause(String text) {
		super(text);
	}

	protected DefaultCause(final SignedTextData signedTextData) {
		super(signedTextData);
	}
}
