package com.tahkeh.loginmessage.methods.impl;

import java.util.Comparator;

import com.tahkeh.loginmessage.methods.IfMethod;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.EqualCheck;

public class IfCheckerMethod extends IfMethod<Variables> {

	private final EqualCheck<? super String> checker;

	public IfCheckerMethod(EqualCheck<? super String> checker, boolean inverted) {
		super(2, inverted);
		this.checker = checker;
	}

	public IfCheckerMethod(Comparator<String> checker, boolean inverted) {
		this((EqualCheck<String>) new EqualCheck.ComparatorEqualChecker<String>(checker), inverted);
	}

	@Override
	protected Boolean match(Parameter[] preValues, Variables globalParameters) {
		return this.checker.equals(preValues[0].parse(), preValues[1].parse());
	}
}
