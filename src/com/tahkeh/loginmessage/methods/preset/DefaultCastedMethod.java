package com.tahkeh.loginmessage.methods.preset;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.MinecraftUtil;

public abstract class DefaultCastedMethod<V extends Variables, C extends V> extends DefaultMethod<V> {

	private final Class<? extends C> variableClass;

	public DefaultCastedMethod(final Class<? extends C> variableClass, final int paramCount, final int... paramCounts) {
		super(paramCount, paramCounts);
		this.variableClass = variableClass;
	}

	public abstract ParameterType innerCall(Parameter[] parameters, C globalParameters);

	@Override
	public final ParameterType call(Parameter[] parameters, int depth, V globalParameters) {
		C castedParameters = MinecraftUtil.cast(variableClass, globalParameters);
		if (castedParameters != null) {
			return this.innerCall(parameters, castedParameters);
		} else {
			return null;
		}
	}

}
