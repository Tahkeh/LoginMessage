package com.tahkeh.loginmessage.methods.impl;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.ArrayParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterTypeFactory;
import com.tahkeh.loginmessage.methods.preset.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class ArrayMethod extends DefaultNamedMethod<Variables> implements ParameterTypeFactory {

	public static final ArrayMethod INSTANCE = new ArrayMethod();

	public ArrayMethod() {
		super("array", 0, -1);
	}

	@Override
	public ParameterType call(Parameter[] parameters, int depth, Variables globalParameters) {
		return this.create(parameters, globalParameters);
	}

	@Override
	public ParameterType create(Parameter[] parameters, Variables variables) {
		final ParameterType[] types = new ParameterType[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			types[i] = parameters[i].parse();
		}
		return new ArrayParameterType(types);
	}

}
