package com.tahkeh.loginmessage.methods.impl;

import java.util.HashMap;
import java.util.Map;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.BooleanParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.DoubleParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.LongParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterTypeFactory;
import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.MinecraftUtil;

public class CreateMethod extends DefaultNamedMethod<Variables> {

	private final static Map<String, ParameterTypeFactory> DEFAULT_FACTORIES = new HashMap<String, ParameterTypeFactory>(5);

	static {
		DEFAULT_FACTORIES.put("long", LongParameterType.LONG_PARAMETER_TYPE_FACTORY);
		DEFAULT_FACTORIES.put("string", StringParameterType.STRING_PARAMETER_TYPE_FACTORY);
		DEFAULT_FACTORIES.put("double", DoubleParameterType.DOUBLE_PARAMETER_TYPE_FACTORY);
		DEFAULT_FACTORIES.put("boolean", BooleanParameterType.BOOLEAN_PARAMETER_TYPE_FACTORY);
		DEFAULT_FACTORIES.put("array", ArrayMethod.INSTANCE);
	}

	private final Map<String, ParameterTypeFactory> factories = new HashMap<String, ParameterTypeFactory>();

	public CreateMethod() {
		super("create", -1);
	}

	public static ParameterTypeFactory getDefaultFactory(final String name) {
		return DEFAULT_FACTORIES.get(name.toLowerCase());
	}

	public ParameterTypeFactory getFactory(final String name) {
		ParameterTypeFactory factory = getDefaultFactory(name);
		if (factory == null) {
			return this.factories.get(name.toLowerCase());
		} else {
			return factory;
		}
	}

	@Override
	public ParameterType call(Parameter[] parameters, int depth, Variables globalParameters) {
		if (parameters.length >= 1) {
			ParameterTypeFactory factory = getFactory(parameters[0].parse().asString());
			if (factory != null) {
				return factory.create(MinecraftUtil.subArray(parameters, 1), globalParameters);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
