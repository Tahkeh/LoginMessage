package com.tahkeh.loginmessage.methods.preset;

import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.variables.Variables;

public abstract class EmptyMethod extends OriginalMethod<Variables> {

	public EmptyMethod(final String defaultName) {
		super(defaultName);
	}

	@Override
	protected ParameterType call(Variables globalParameters) {
		return this.call();
	}

	protected abstract ParameterType call();
}
