package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;
import com.tahkeh.loginmessage.methods.variables.bukkit.KickVariables;

public class KickReasonMethod extends OriginalMethod<Variables> {

	public KickReasonMethod() {
		super("reason");
	}

	@Override
	protected StringParameterType call(Variables globalParameters) {
		if (globalParameters instanceof KickVariables) {
			return new StringParameterType(((KickVariables) globalParameters).reason);
		} else {
			return null;
		}
	}

}
