package com.tahkeh.loginmessage.methods.parameter.types;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.preset.DefaultMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class BooleanParameterType extends NativeParameterType {

	private final boolean value;

	public BooleanParameterType(final boolean value) {
		this.value = value;
	}

	@Override
	public Double asDouble() {
		return this.asLong().doubleValue();
	}

	@Override
	public Long asLong() {
		return this.value ? 1L : 0L;
	}

	@Override
	public String asString() {
		return Boolean.toString(this.value);
	}

	@Override
	public Boolean asBoolean() {
		return this.value;
	}

	public static final BooleanParameterTypeFactory BOOLEAN_PARAMETER_TYPE_FACTORY = new BooleanParameterTypeFactory();
	public static class BooleanParameterTypeFactory implements ParameterTypeFactory {

		@Override
		public ParameterType create(Parameter[] parameters, Variables variables) {
			if (parameters.length == 1) {
				Boolean b = DefaultMethod.parseAsBoolean(parameters[0].parse().asString());
				if (b != null) {
					return new BooleanParameterType(b);
				}
			}
			return null;
		}

	}
}
