package com.tahkeh.loginmessage.methods;

public class AliasMethod implements Method {

	private final String result;
	private final int paramCount;

	public AliasMethod(final String result, final int paramCount) {
		this.result = result;
		this.paramCount = paramCount;
	}

	@Override
	public String call(String... parameters) {
		if (paramCount == parameters.length) {
			String result = this.result;
			for (int i = 0; i < parameters.length; i++) {
				result = result.replaceAll("\\$" + i + ";", parameters[i]);
			}
			return result;
		} else {
			return null;
		}
	}

	@Override
	public boolean recursive() {
		return true;
	}

}
