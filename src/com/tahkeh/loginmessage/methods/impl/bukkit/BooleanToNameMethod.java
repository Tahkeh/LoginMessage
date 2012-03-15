package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class BooleanToNameMethod extends DefaultNamedMethod<Variables> {

	private final Message message;

	public BooleanToNameMethod(Message message) {
		super("booltoname", 1);
		this.message = message;
	}

	@Override
	public ParameterType call(Parameter[] parameters, int depth, Variables globalParameters) {
		if (parameters.length == 1) {
			final Boolean bool = parameters[0].parse().asBoolean();
			if (bool != null) {
				return new StringParameterType(this.message.booleanToName(bool));
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
