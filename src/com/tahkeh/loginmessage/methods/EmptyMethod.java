package com.tahkeh.loginmessage.methods;

import com.tahkeh.loginmessage.methods.variables.Variables;

public abstract class EmptyMethod extends OriginalMethod<Variables> {

	public EmptyMethod(final String defaultName) {
		super(defaultName);
	}

	@Override
	protected String call(Variables globalParameters) {
		return this.call();
	}

	protected abstract String call();
}
