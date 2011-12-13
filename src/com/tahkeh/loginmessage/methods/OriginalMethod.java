package com.tahkeh.loginmessage.methods;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

/**
 * Simple class for all original methods (without any arguments). Redirect
 * all calls to {@link OriginalMethod#call(String, Variables)}.
 */
public abstract class OriginalMethod<V extends Variables> extends DefaultNamedMethod<V> {

	public OriginalMethod(final String defaultName) {
		super(false, defaultName, 0);
	}

	@Override
	public final String call(Parameter[] parameters, V globalParameters) {
		if (parameters.length == 0) {
			return this.call(globalParameters);
		} else {
			return null;
		}
	}

	protected abstract String call(V globalParameters);
}
