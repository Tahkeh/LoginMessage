package com.tahkeh.loginmessage.methods.parameter.types;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.preset.DefaultMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class LongParameterType extends NativeParameterType {

	private final long value;

	public LongParameterType(final long value) {
		this.value = value;
	}

	@Override
	public Double asDouble() {
		return new Double(this.value);
	}

	@Override
	public Long asLong() {
		return this.value;
	}

	@Override
	public String asString() {
		return Long.toString(this.value);
	}

	@Override
	public Boolean asBoolean() {
		return this.value != 0;
	}

	public static final LongParameterTypeFactory LONG_PARAMETER_TYPE_FACTORY = new LongParameterTypeFactory();
	public static class LongParameterTypeFactory implements ParameterTypeFactory {

		@Override
		public ParameterType create(Parameter[] parameters, Variables variables) {
			if (parameters.length == 1) {
				Long l = DefaultMethod.parseAsLong(parameters[0].parse().asString());
				if (l != null) {
					return new LongParameterType(l);
				}
			}
			return null;
		}

	}
}
