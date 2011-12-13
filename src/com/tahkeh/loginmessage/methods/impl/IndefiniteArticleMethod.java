package com.tahkeh.loginmessage.methods.impl;

import com.tahkeh.loginmessage.methods.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class IndefiniteArticleMethod extends DefaultNamedMethod<Variables> {

	public IndefiniteArticleMethod() {
		super(true, "an", 1);
	}

	@Override
	public String call(Parameter[] parameters, Variables globalParameters) {
		if (parameters.length == 1) {
			char letter = parameters[0].parse().trim().charAt(0);
			final boolean vowel = (letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u');
			return (vowel ? "an " : "a ") + parameters[0];
		} else {
			return null;
		}
	}

}
