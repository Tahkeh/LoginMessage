package com.tahkeh.loginmessage.methods.parameter.types;

public interface ParameterType {

	boolean isArray();

	ParameterType[] getArray();

	Double asDouble();

	Long asLong();

	String asString();

	String asParsableString(final String prefix);

	Boolean asBoolean();
}
