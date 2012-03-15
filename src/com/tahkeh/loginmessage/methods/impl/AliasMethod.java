package com.tahkeh.loginmessage.methods.impl;

import com.tahkeh.loginmessage.methods.MethodParser;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.preset.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class AliasMethod<V extends Variables> extends DefaultNamedMethod<V> {

	private final String result;
	private final MethodParser<V> parser;

	public AliasMethod(final String result, final int paramCount, final String name, final MethodParser<V> parser) {
		super(name, paramCount);
		this.result = result;
		this.parser = parser;
	}

	public static <V extends Variables> AliasMethod<V> create(final String result, final int paramCount, final String name, final MethodParser<V> parser) {
		return new AliasMethod<V>(result, paramCount, name, parser);
	}

	@Override
	public ParameterType call(Parameter[] parameters, int depth, V globalParameters) {
		if (this.getParamCounts()[0] == parameters.length) {
			String result = this.result;
			for (int i = 0; i < parameters.length; i++) {
				result = result.replaceAll("\\$" + i + ";", parameters[i].parse().asParsableString(this.parser.getPrefix()));
			}
			return this.parser.parseLine(result, globalParameters, depth + 1);
		} else {
			return null;
		}
	}

	public AliasMethod<V> register() {
		super.register(this.parser);
		return this;
	}

	public AliasMethod<V> unregister() {
		super.unregister(this.parser);
		return this;
	}
}
