package com.tahkeh.loginmessage.methods.impl;

import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.EmptyMethod;

public final class ConstantMethod extends EmptyMethod {

	public static final ConstantMethod NULL_METHOD = new ConstantMethod(null, "null");

	private final StringParameterType value;

	public ConstantMethod(final String value, final String defaultName) {
		super(defaultName);
		this.value = value != null ? new StringParameterType(value) : null;
	}

	@Override
	protected StringParameterType call() {
		return this.value;
	}

}
