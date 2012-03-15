package com.tahkeh.loginmessage.methods.preset;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.DoubleParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.MinecraftUtil;

public abstract class DoubleMethod<V extends Variables, C extends V> extends DefaultCastedNamedMethod<V, C> {

	public DoubleMethod(final String defaultName, final Class<? extends C> variableClass) {
		super(defaultName, variableClass, 0, 1, 2);
	}

	@Override
	public final ParameterType innerCall(Parameter[] parameters, C globalParameters) {
		if (parameters.length > 2) {
			return null;
		} else {
			Double value = getValue(globalParameters);
			if (value != null) {
				Long minDecimals = 0L;
				Long maxDecimals = 0L;
				switch (parameters.length) {
				case 2:
					minDecimals = parameters[1].parse().asLong();
				case 1:
					maxDecimals = parameters[0].parse().asLong();
					break;
				}
				if (minDecimals != null && maxDecimals != null) {
					return new DoubleParameterType(value, MinecraftUtil.getFormatWithMinimumDecimals(minDecimals.intValue(), maxDecimals.intValue()));
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}

	public abstract Double getValue(C globalParameters);
}
