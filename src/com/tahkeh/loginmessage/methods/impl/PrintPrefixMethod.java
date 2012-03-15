package com.tahkeh.loginmessage.methods.impl;

import com.tahkeh.loginmessage.methods.MethodParser;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.DefaultMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.MinecraftUtil;

public class PrintPrefixMethod extends DefaultMethod<Variables> {

	private final MethodParser<?> parser;

	public PrintPrefixMethod(final MethodParser<?> parser) {
		super(0);
		this.parser = parser;
	}

	@Override
	public ParameterType call(Parameter[] parameters, int depth, Variables globalParameters) {
		if (parameters.length == 0) {
			return new StringParameterType(this.parser.getPrefix());
		} else {
			return null;
		}
	}

	public PrintPrefixMethod register() {
		if (MinecraftUtil.isSet(this.parser.getPrefix())) {
			super.register(this.parser.getPrefix(), this.parser);
		}
		return this;
	}

	public PrintPrefixMethod unregister() {
		if (MinecraftUtil.isSet(this.parser.getPrefix())) {
			super.unregister(this.parser.getPrefix(), this.parser);
		}
		return this;
	}
}
