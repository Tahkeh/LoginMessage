package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;
import com.tahkeh.loginmessage.methods.variables.bukkit.KickVariables;

public class KickReasonMethod extends OriginalMethod<Variables> {

	public KickReasonMethod() {
		super("reason");
	}

	@Override
	protected String call(Variables globalParameters) {
		if (globalParameters instanceof KickVariables) {
			return ((KickVariables) globalParameters).reason;
		} else {
			return null;
		}
	}

}
