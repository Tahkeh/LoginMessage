package com.tahkeh.loginmessage.methods.impl;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.LongParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.preset.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class AddMethod extends DefaultNamedMethod<Variables> {

	public AddMethod() {
		super("add", -1);
	}

	@Override
	public ParameterType call(Parameter[] parameters, int depth, Variables globalParameters) {
		long sum = 0L;
		for (Parameter parameter : parameters) {
			sum += parameter.parse().asLong();
		}
		return new LongParameterType(sum);
	}

}
