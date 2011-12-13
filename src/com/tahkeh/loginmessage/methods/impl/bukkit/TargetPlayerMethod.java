package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerNotFoundVariables;

public class TargetPlayerMethod extends OriginalMethod<Variables> {

	public TargetPlayerMethod() {
		super("target");
	}

	@Override
	protected String call(Variables globalParameters) {
		if (globalParameters instanceof PlayerNotFoundVariables) {
			return ((PlayerNotFoundVariables) globalParameters).targetName;
		} else {
			return null;
		}
	}

}
