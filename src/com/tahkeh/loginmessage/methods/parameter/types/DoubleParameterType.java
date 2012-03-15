package com.tahkeh.loginmessage.methods.parameter.types;

import java.text.DecimalFormat;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.MinecraftUtil;

public class DoubleParameterType extends NativeParameterType {

	private final double value;
	private final DecimalFormat format;

	public DoubleParameterType(final double value, final DecimalFormat format) {
		this.value = value;
		this.format = format;
	}

	@Override
	public Double asDouble() {
		return this.value;
	}

	@Override
	public Long asLong() {
		return (long) Math.round(this.value);
	}

	@Override
	public String asString() {
		return this.format.format(this.value);
	}

	@Override
	public Boolean asBoolean() {
		return !MinecraftUtil.equals(this.value, 0);
	}

	public static final DoubleParameterTypeFactory DOUBLE_PARAMETER_TYPE_FACTORY = new DoubleParameterTypeFactory();
	public static class DoubleParameterTypeFactory implements ParameterTypeFactory {

		@Override
		public ParameterType create(Parameter[] parameters, Variables variables) {
			Long minDecimals = 0L;
			Long maxDecimals = 0L;
			String value = null;
			switch (parameters.length) {
			case 3:
				minDecimals = parameters[2].parse().asLong();
			case 2:
				maxDecimals = parameters[1].parse().asLong();
			case 1:
				value = parameters[0].parse().asString();
				break;
			}
			if (minDecimals != null && maxDecimals != null && value != null) {
				Double d = MinecraftUtil.tryAndGetDouble(value);
				if (d != null) {
					return new DoubleParameterType(d, MinecraftUtil.getFormatWithMinimumDecimals(minDecimals.intValue(), maxDecimals.intValue()));
				} else {
					return null;
				}
			} else {
				return null;
			}
		}

	}
}
