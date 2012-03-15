package com.tahkeh.loginmessage.methods.preset.bukkit;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.preset.CaseMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public abstract class BooleanMethod extends CaseMethod<BukkitVariables, PlayerVariables> {

	private final Message message;

	public BooleanMethod(final int preValueCount, final Message message, final String defaultName) {
		super(preValueCount, defaultName, PlayerVariables.class);
		this.message = message;
	}

	@Override
	protected final String call(final Parameter[] preValues, final PlayerVariables globalParameters) {
		final Boolean bool = this.getBoolean(preValues, globalParameters);
		if (bool == null) {
			return null;
		} else {
			return this.message.booleanToName(bool);
		}
	}

	protected abstract Boolean getBoolean(final Parameter[] preValues, final PlayerVariables globalParameters);
}
