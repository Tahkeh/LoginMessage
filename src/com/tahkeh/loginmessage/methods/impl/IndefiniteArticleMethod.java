package com.tahkeh.loginmessage.methods.impl;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class IndefiniteArticleMethod extends DefaultNamedMethod<Variables> {

	public IndefiniteArticleMethod() {
		super("an", 1);
	}

	@Override
	public ParameterType call(Parameter[] parameters, int depth, Variables globalParameters) {
		if (parameters.length == 1) {
			char letter = parameters[0].parse().asString().trim().charAt(0);
			final boolean vowel = (letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u');
			return new StringParameterType((vowel ? "an " : "a ") + parameters[0]);
		} else {
			return null;
		}
	}

}
