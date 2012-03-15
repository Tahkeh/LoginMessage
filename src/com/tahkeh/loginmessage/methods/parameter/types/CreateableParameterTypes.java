package com.tahkeh.loginmessage.methods.parameter.types;

public abstract class CreateableParameterTypes implements ParameterType {

	private final String name;

	public CreateableParameterTypes(final String name) {
		this.name = name;
	}

	protected abstract void getContent(final StringBuilder builder);

	@Override
	public String asParsableString(String prefix) {
		final StringBuilder builder = new StringBuilder(prefix).append("create(").append(name).append(", ");
		this.getContent(builder);
		return builder.append(")").toString();
	}
}
