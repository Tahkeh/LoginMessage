package com.tahkeh.loginmessage.matcher.deathcauses;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class NonEntityDeathCause extends DefaultDeathCause{

	public NonEntityDeathCause(String text) {
		super(text);
	}
	
	public NonEntityDeathCause(SignedTextData signedTextData) {
		super(signedTextData);
	}

	public boolean match(DamageCause cause) {
		return cause.toString().toLowerCase().equals(this.signedTextData.unsignedText);
	}
}
