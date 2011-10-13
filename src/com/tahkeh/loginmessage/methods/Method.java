package com.tahkeh.loginmessage.methods;

public interface Method {

	/**
	 * Executes the method and returns null if the parameters are invalid.
	 * @param parameters the parameters.
	 * @return the parsed string or null if invalid.
	 */
	String call(String... parameters);

	/**
	 * Defines if this method has recursive calls.
	 * @return if this method has recursive calls.
	 */
	boolean recursive();
}
