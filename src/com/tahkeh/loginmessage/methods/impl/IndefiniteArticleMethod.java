package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.DefaultMethod;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.DefaultVariables;

public class IndefiniteArticleMethod extends DefaultMethod {

	public IndefiniteArticleMethod() {
		super(true, 1);
	}

	@Override
	public String call(OfflinePlayer player, String event, Parameter[] parameters, DefaultVariables globalParameters) {
		if (parameters.length == 1) {
			char letter = parameters[0].parse().trim().charAt(0);
			final boolean vowel = (letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u');
			return (vowel ? "an " : "a ") + parameters[0];
		} else {
			return null;
		}
	}

}
