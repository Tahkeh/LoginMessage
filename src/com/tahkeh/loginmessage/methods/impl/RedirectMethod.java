package com.tahkeh.loginmessage.methods.impl;


import com.tahkeh.loginmessage.methods.Method;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class RedirectMethod<V extends Variables> implements Method<V> {

	private final Method<? super V> redirected;

	public RedirectMethod(final Method<? super V> redirected) {
		this.redirected = redirected;
	}

	@Override
	public String call(Parameter[] parameters, V globalParameters) {
		return this.redirected.call(parameters, globalParameters);
	}

	@Override
	public boolean isRecursive() {
		return this.redirected.isRecursive();
	}
}
