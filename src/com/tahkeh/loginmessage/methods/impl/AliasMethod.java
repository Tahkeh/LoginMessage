package com.tahkeh.loginmessage.methods.impl;


import com.tahkeh.loginmessage.methods.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class AliasMethod extends DefaultNamedMethod<Variables> {

	private final String result;

	public AliasMethod(final String result, final int paramCount, final String name) {
		super(true, name, paramCount);
		this.result = result;
	}

	@Override
	public String call(Parameter[] parameters, Variables globalParameters) {
		if (this.getParamCounts()[0] == parameters.length) {
			String result = this.result;
			for (int i = 0; i < parameters.length; i++) {
				result = result.replaceAll("\\$" + i + ";", parameters[i].parse());
			}
			return result;
		} else {
			return null;
		}
	}

}
