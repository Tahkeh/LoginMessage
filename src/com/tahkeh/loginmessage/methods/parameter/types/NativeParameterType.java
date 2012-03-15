package com.tahkeh.loginmessage.methods.parameter.types;

public abstract class NativeParameterType extends NonArrayParameterType {

	@Override
	public String asParsableString(String prefix) {
		return this.asString();
	}
}
