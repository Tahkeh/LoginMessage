package com.tahkeh.loginmessage.methods.parameter.types;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.preset.DefaultMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.MinecraftUtil;

public class StringParameterType extends NativeParameterType {

	public static final StringParameterType EMPTY_PARAMETER_TYPE = new StringParameterType("");

	private final String value;

	public StringParameterType(final String value) {
		this.value = value;
	}

	@Override
	public Double asDouble() {
		return MinecraftUtil.tryAndGetDouble(this.value);
	}

	@Override
	public Long asLong() {
		return DefaultMethod.parseAsLong(this.value);
	}

	@Override
	public String asString() {
		return this.value;
	}

	@Override
	public Boolean asBoolean() {
		return DefaultMethod.parseAsBoolean(this.value);
	}

	public static final StringParameterTypeFactory STRING_PARAMETER_TYPE_FACTORY = new StringParameterTypeFactory();
	public static class StringParameterTypeFactory implements ParameterTypeFactory {

		@Override
		public ParameterType create(Parameter[] parameters, Variables variables) {
			if (parameters.length == 1) {
				String value = parameters[0].parse().asString();
				if (value != null) {
					return new StringParameterType(value);
				}
			}
			return null;
		}

	}
}
