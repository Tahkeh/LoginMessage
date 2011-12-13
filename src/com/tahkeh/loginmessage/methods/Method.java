package com.tahkeh.loginmessage.methods;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

public interface Method<V extends Variables> {

	/**
	 * Executes the method and returns null if the parameters are invalid.
	 * @param parameters the parameters.
	 * @return the parsed string or null if invalid.
	 */
	String call(Parameter[] parameters, V globalParameters);

	/**
	 * Defines if this method has recursive calls.
	 * @return if this method has recursive calls.
	 */
	boolean isRecursive();
}
