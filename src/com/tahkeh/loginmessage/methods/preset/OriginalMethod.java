package com.tahkeh.loginmessage.methods.preset;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.variables.Variables;

/**
 * Simple class for all original methods (without any arguments). Redirect
 * all calls to {@link OriginalMethod#call(String, int, Variables)}.
 */
public abstract class OriginalMethod<V extends Variables> extends DefaultNamedMethod<V> {

	public OriginalMethod(final String defaultName) {
		super(defaultName, 0);
	}

	@Override
	public final ParameterType call(Parameter[] parameters, int depth, V globalParameters) {
		if (parameters.length == 0) {
			return this.call(globalParameters);
		} else {
			return null;
		}
	}

	protected abstract ParameterType call(V globalParameters);
}
