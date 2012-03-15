package com.tahkeh.loginmessage.methods.impl;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.LongParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.preset.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class SubtractMethod extends DefaultNamedMethod<Variables> {

	public SubtractMethod() {
		super("subtract", -1);
	}

	@Override
	public ParameterType call(Parameter[] parameters, int depth, Variables globalParameters) {
		long difference = 0L;
		for (int i = 0; i < parameters.length; i++) {
			difference -= parameters[i].parse().asLong();
		}
		return new LongParameterType(difference);
	}

}
