package com.tahkeh.loginmessage.methods.parameter;

import com.tahkeh.loginmessage.methods.MethodParser;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class ParsedParameter<V extends Variables> implements Parameter {

	private final MethodParser<? super V> parser;
	private final String parameterValue;
	private final V variable;
	private final int depth;

	public ParsedParameter(final MethodParser<? super V> parser, final String parameterValue, final V variable, final int depth) {
		this.parser = parser;
		this.parameterValue = parameterValue;
		this.variable = variable;
		this.depth = depth;
	}

	public static <V extends Variables> ParsedParameter<V> create(final MethodParser<? super V> parser, final String parameterValue, final V variable, final int depth) {
		return new ParsedParameter<V>(parser, parameterValue, variable, depth);
	}

	@Override
	public String parse() {
		return this.parser.parseLine(this.parameterValue, this.variable, this.depth);
	}

}
