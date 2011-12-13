package com.tahkeh.loginmessage.methods;

import java.util.Arrays;

import com.tahkeh.loginmessage.methods.parameter.FinalParameter;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

public abstract class IfMethod<V extends Variables> implements Method<V> {

	private final int preValueCount;
	private final boolean inverted;

	protected IfMethod(final int preValueCount, final boolean inverted) {
		this.preValueCount = Math.max(0, preValueCount);
		this.inverted = inverted;
	}

	@Override
	public final String call(Parameter[] parameters, V globalParameters) {
		Parameter match = FinalParameter.EMPTY_PARAMETER;
		Parameter noMatch = FinalParameter.EMPTY_PARAMETER;
		switch (parameters.length - this.preValueCount) {
		case 2:
			noMatch = parameters[this.preValueCount + 1];
		case 1:
			match = parameters[this.preValueCount];
			break;
		default:
			return null;
		}
		return this.match(Arrays.copyOf(parameters, this.preValueCount), globalParameters) != this.inverted ? match.parse() : noMatch.parse();
	}

	protected abstract Boolean match(Parameter[] preValues, V globalParameters);

	@Override
	public boolean isRecursive() {
		return true;
	}

}
