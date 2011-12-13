package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;
import com.tahkeh.loginmessage.methods.variables.bukkit.DeathVariables;

public class DeathEntityMethod extends OriginalMethod<Variables> {

	public DeathEntityMethod() {
		super("dentity");
	}

	@Override
	protected String call(Variables globalParameters) {
		if (globalParameters instanceof DeathVariables) {
			return ((DeathVariables) globalParameters).entity;
		} else {
			return null;
		}
	}

}
