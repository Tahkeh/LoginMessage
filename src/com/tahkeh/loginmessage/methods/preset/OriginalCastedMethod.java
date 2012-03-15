package com.tahkeh.loginmessage.methods.preset;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.variables.Variables;

public abstract class OriginalCastedMethod<V extends Variables, C extends V> extends DefaultCastedNamedMethod<V, C> {

	public OriginalCastedMethod(final String defaultName, final Class<? extends C> variableClass) {
		super(defaultName, variableClass, 0);
	}

	@Override
	public final ParameterType innerCall(Parameter[] parameters, C globalParameters) {
		if (parameters.length == 0) {
			return this.call(globalParameters);
		} else {
			return null;
		}
	}

	protected abstract ParameterType call(C globalParameters);

}
