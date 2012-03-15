package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerNotFoundVariables;

public class TargetPlayerMethod extends OriginalMethod<Variables> {

	public TargetPlayerMethod() {
		super("target");
	}

	@Override
	protected StringParameterType call(Variables globalParameters) {
		if (globalParameters instanceof PlayerNotFoundVariables) {
			return new StringParameterType(((PlayerNotFoundVariables) globalParameters).targetName);
		} else {
			return null;
		}
	}

}
