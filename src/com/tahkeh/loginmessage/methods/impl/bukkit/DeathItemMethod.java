package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;
import com.tahkeh.loginmessage.methods.variables.bukkit.DeathVariables;

public class DeathItemMethod extends OriginalMethod<Variables> {

	public DeathItemMethod() {
		super("ditem");
	}

	@Override
	protected String call(Variables globalParameters) {
		if (globalParameters instanceof DeathVariables) {
			return ((DeathVariables) globalParameters).item;
		} else {
			return null;
		}
	}
}
