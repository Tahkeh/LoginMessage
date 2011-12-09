package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.Message;

public abstract class BooleanMethod extends CaseMethod {

	private final Message message;

	public BooleanMethod(final Message message, final String defaultName) {
		super(defaultName);
		this.message = message;
	}

	@Override
	protected final String call(OfflinePlayer player, String event) {
		Boolean bool = this.getBoolean(player, event);
		if (bool == null) {
			return null;
		} else {
			return this.message.booleanToName(bool);
		}
	}

	protected abstract Boolean getBoolean(OfflinePlayer player, String event);
}
