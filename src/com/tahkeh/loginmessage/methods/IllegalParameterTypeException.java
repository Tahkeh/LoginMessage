package com.tahkeh.loginmessage.methods;

public class IllegalParameterTypeException extends MethodException {

	private static final long serialVersionUID = 7493373797681392841L;

	public IllegalParameterTypeException(final String methodName, final int index, final String value) {
		super("Illegal parameter type (" + index + ") in method '" + methodName + "' with value '" + value + "'!");
	}
}
