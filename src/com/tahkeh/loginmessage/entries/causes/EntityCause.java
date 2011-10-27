package com.tahkeh.loginmessage.entries.causes;

public class EntityCause extends DefaultCause {

	public EntityCause(String text) {
		super(text);
	}

	public boolean match(String cause) {
		cause = "Craft" + cause;
		String path = "org.bukkit.craftbukkit.entity.";
		try {
			return Class.forName(path + cause).isAssignableFrom(Class.forName(path + this.value));
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

}
