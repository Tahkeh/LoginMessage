package com.tahkeh.loginmessage.matcher.causes;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.tahkeh.loginmessage.matcher.DefaultMatcher.SignedTextData;

public class EnumCause implements Cause {

	private final DamageCause damageCause;
	private final boolean positive;

	public EnumCause(final DamageCause damageCause, final boolean positive) {
		this.damageCause = damageCause;
		this.positive = positive;
	}

	public static EnumCause create(final String text) {
		final SignedTextData signedTextData = new SignedTextData(text);
		DamageCause damageCause = DamageCause.valueOf(signedTextData.unsignedText);
		if (damageCause != null) {
			return new EnumCause(damageCause, signedTextData.positive);
		} else {
			return null;
		}
	}

	@Override
	public boolean isPositive() {
		return this.positive;
	}

	@Override
	public boolean match(EntityDamageEvent parameter) {
		return parameter.getCause() == this.damageCause;
	}

}
