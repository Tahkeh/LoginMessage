package com.tahkeh.loginmessage.methods.parameter;

import com.tahkeh.loginmessage.methods.MethodParser;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class OnceParsedParameter<V extends Variables> extends ParsedParameter<V> {

	private ParameterType parsedParameter = null;
	private boolean parsed = false;

	public OnceParsedParameter(final MethodParser<? super V> parser, final String parameterValue, final V variable, final int depth) {
		super(parser, parameterValue, variable, depth);
	}

	public static <V extends Variables> OnceParsedParameter<V> create(final MethodParser<? super V> parser, final String parameterValue, final V variable, final int depth) {
		return new OnceParsedParameter<V>(parser, parameterValue, variable, depth);
	}

	@Override
	public ParameterType parse() {
		if (!this.parsed) {
			this.parsedParameter = super.parse();
			this.parsed = true;
		}
		return this.parsedParameter;
	}
}
