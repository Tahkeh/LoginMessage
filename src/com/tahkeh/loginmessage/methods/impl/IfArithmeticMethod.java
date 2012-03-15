package com.tahkeh.loginmessage.methods.impl;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.preset.IfMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.EqualCheck;

public class IfArithmeticMethod extends IfMethod<Variables> {

	private final EqualCheck<? super Double> checker;

	public IfArithmeticMethod(final EqualCheck<? super Double> checker) {
		super(2, false);
		this.checker = checker;
	}

	@Override
	protected Boolean match(Parameter[] preValues, Variables globalParameters) {
		Double a = preValues[0].parse().asDouble();
		Double b = preValues[1].parse().asDouble();
		if (a != null && b != null) {
			return this.checker.equals(a, b);
		} else {
			return null;
		}
	}

}
