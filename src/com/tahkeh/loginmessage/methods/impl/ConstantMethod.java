package com.tahkeh.loginmessage.methods.impl;

import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public final class ConstantMethod extends OriginalMethod<Variables> {

	private final String value;

	public ConstantMethod(final String value, final String defaultName) {
		super(defaultName);
		this.value = value;
	}

	@Override
	protected String call(Variables globalParameters) {
		return this.value;
	}

}
