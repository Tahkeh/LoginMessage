package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class ColorMethod extends DefaultNamedMethod<Variables> {

	public ColorMethod() {
		super("color", 1);
	}

	@Override
	public ParameterType call(Parameter[] parameters, int depth, Variables globalParameters) {
		if (parameters.length == 1) {
			Long l = parameters[0].parse().asLong();
			if (l != null) {
				return new StringParameterType(Message.SECTION_SIGN + Long.toHexString(l));
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
