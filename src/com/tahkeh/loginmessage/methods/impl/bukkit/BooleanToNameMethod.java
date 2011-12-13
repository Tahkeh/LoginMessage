package com.tahkeh.loginmessage.methods.impl.bukkit;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.DefaultMethod;
import com.tahkeh.loginmessage.methods.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class BooleanToNameMethod extends DefaultNamedMethod<Variables> {

	private final Message message;

	public BooleanToNameMethod(Message message) {
		super(true, "booltoname", 1);
		this.message = message;
	}

	@Override
	public String call(Parameter[] parameters, Variables globalParameters) {
		if (parameters.length == 1) {
			final Boolean bool = DefaultMethod.parseAsBoolean(parameters[0].parse());
			if (bool != null) {
				return this.message.booleanToName(bool);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
