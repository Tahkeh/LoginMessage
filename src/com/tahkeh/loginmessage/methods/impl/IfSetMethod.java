package com.tahkeh.loginmessage.methods.impl;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.preset.IfMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class IfSetMethod extends IfMethod<Variables> {

	public IfSetMethod(final boolean inverted) {
		super(1, inverted);
	}

	@Override
	protected Boolean match(Parameter[] preValues, Variables globalParameters) {
		return preValues[0].parse() != null;
	}
}
