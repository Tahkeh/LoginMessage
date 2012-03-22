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
			final String item = ((DeathVariables) globalParameters).item;
			if (item != null) {
				return new StringParameterType(item);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
}
