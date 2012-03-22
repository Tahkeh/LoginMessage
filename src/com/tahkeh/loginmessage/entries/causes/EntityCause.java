package com.tahkeh.loginmessage.entries.causes;

import com.tahkeh.loginmessage.util.ClassResolvers;

public class EntityCause extends DefaultCause {

	public EntityCause(String text) {
		super(text);
	}

	public boolean match(String cause) {
		cause = "Craft" + cause;
		try {
			return Class.forName(ClassResolvers.CRAFTBUKKIT_ENTITY_PREFIX + cause).isAssignableFrom(Class.forName(ClassResolvers.CRAFTBUKKIT_ENTITY_PREFIX + this.signedTextData.unsignedText));
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

}
