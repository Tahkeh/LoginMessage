package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.variables.Variables;

public abstract class BooleanMethod extends CaseMethod {

	private final Message message;

	public BooleanMethod(final Message message, final String defaultName) {
		super(defaultName);
		this.message = message;
	}

	@Override
	protected final String call(OfflinePlayer player, Variables globalParameters) {
		Boolean bool = this.getBoolean(player, globalParameters);
		if (bool == null) {
			return null;
		} else {
			return this.message.booleanToName(bool);
		}
	}

	protected abstract Boolean getBoolean(OfflinePlayer player, Variables globalParameters);
}
