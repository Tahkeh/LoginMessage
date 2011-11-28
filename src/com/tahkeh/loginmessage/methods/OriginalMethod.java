package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.DefaultVariables;

/**
 * Simple class for all original methods (without any arguments). Redirect
 * all calls to {@link OriginalMethod#call(OfflinePlayer, String, DefaultVariables)}.
 */
public abstract class OriginalMethod extends DefaultMethod {

	public OriginalMethod() {
		super(false, 0);
	}

	@Override
	public final String call(OfflinePlayer player, String event, Parameter[] parameters, DefaultVariables globalParameters) {
		if (parameters.length == 0) {
			return call(player, event, globalParameters);
		} else {
			return null;
		}
	}

	protected abstract String call(OfflinePlayer player, String event, DefaultVariables globalParameters);
}
