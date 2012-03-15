package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;
import com.tahkeh.loginmessage.methods.variables.bukkit.DeathVariables;

public class DeathItemMethod extends OriginalMethod<Variables> {

	public DeathItemMethod() {
		super("ditem");
	}

	@Override
	protected StringParameterType call(Variables globalParameters) {
		if (globalParameters instanceof DeathVariables) {
			return new StringParameterType(((DeathVariables) globalParameters).item);
		} else {
			return null;
		}
	}
}
