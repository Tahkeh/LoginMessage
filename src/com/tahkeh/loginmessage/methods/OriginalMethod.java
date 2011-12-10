package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

/**
 * Simple class for all original methods (without any arguments). Redirect
 * all calls to {@link OriginalMethod#call(OfflinePlayer, String, Variables)}.
 */
public abstract class OriginalMethod extends DefaultNamedMethod {

	public OriginalMethod(final String defaultName) {
		super(false, defaultName, 0);
	}

	@Override
	public final String call(OfflinePlayer player, Parameter[] parameters, Variables globalParameters) {
		if (parameters.length == 0) {
			return call(player, globalParameters);
		} else {
			return null;
		}
	}

	protected abstract String call(OfflinePlayer player, Variables globalParameters);
}
