package com.tahkeh.loginmessage.methods.parameter.types;

public abstract class NonArrayParameterType implements ParameterType {

	@Override
	public boolean isArray() {
		return false;
	}

	@Override
	public ParameterType[] getArray() {
		return new ParameterType[] { this };
	}
}
