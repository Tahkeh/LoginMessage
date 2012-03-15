package com.tahkeh.loginmessage.methods.impl;

import com.tahkeh.loginmessage.methods.Method;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class RedirectMethod<V extends Variables> implements Method<V> {

	private final Method<? super V> redirected;

	public RedirectMethod(final Method<? super V> redirected) {
		this.redirected = redirected;
	}

	@Override
	public ParameterType call(Parameter[] parameters, int depth, V globalParameters) {
		return this.redirected.call(parameters, depth + 1, globalParameters);
	}
}
