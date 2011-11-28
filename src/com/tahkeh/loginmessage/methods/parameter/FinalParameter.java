package com.tahkeh.loginmessage.methods.parameter;

public class FinalParameter implements Parameter {

	public static final FinalParameter EMPTY_PARAMETER = new FinalParameter("");
	public static final FinalParameter NULL_PARAMETER = new FinalParameter(null);

	public final String parameterValue;

	public FinalParameter(final String parameterValue) {
		this.parameterValue = parameterValue;
	}

	@Override
	public String parse() {
		return this.parameterValue;
	}

}
