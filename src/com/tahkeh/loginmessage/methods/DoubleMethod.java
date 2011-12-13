package com.tahkeh.loginmessage.methods;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.MinecraftUtil;

public abstract class DoubleMethod<V extends Variables> extends DefaultNamedMethod<V> {

	public DoubleMethod(final String defaultName) {
		super(true, defaultName, 0, 1, 2);
	}

	@Override
	public final String call(Parameter[] parameters, V globalParameters) {
		if (parameters.length > 2) {
			return null;
		} else {
			Double value = getValue(globalParameters);
			if (value != null) {
				int minDecimals = 0;
				int maxDecimals = 0;
				switch (parameters.length) {
				case 2:
					minDecimals = DefaultMethod.parseAsInteger(parameters[1].parse());
				case 1:
					maxDecimals = DefaultMethod.parseAsInteger(parameters[0].parse());
					break;
				}
				return MinecraftUtil.getFormatWithMinimumDecimals(minDecimals, maxDecimals).format(value);
			} else {
				return null;
			}
		}
	}

	public abstract Double getValue(V globalParameters);
}
