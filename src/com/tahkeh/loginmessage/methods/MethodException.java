package com.tahkeh.loginmessage.methods;

public class MethodException extends Exception {

	private static final long serialVersionUID = 1281135283952262429L;

	public MethodException(String message) {
		super(message);
	}

	public MethodException(String message, Throwable cause) {
		super(message, cause);
	}
}
