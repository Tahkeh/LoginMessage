package com.tahkeh.loginmessage.methods;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.variables.Variables;

public interface Method<V extends Variables> {

	/**
	 * Executes the method and returns null if the parameters are invalid.
	 * @param parameters the parameters.
	 * @param depth the depth of this call.
	 * @return the parsed string or null if invalid.
	 */
	ParameterType call(Parameter[] parameters, final int depth, V globalParameters);
}
