package com.tahkeh.loginmessage.methods.preset;

import java.util.ArrayList;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.LongParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.variables.Variables;

public abstract class MinMaxMethod extends DefaultNamedMethod<Variables> {

	private final boolean first;

	protected MinMaxMethod(final boolean first, final String defaultName) {
		super(first ? defaultName : "n" + defaultName, first ? -1 : -2);
		this.first = first;
	}

	protected abstract boolean compare(final long nHighLowest, final long tested);

	@Override
	public ParameterType call(Parameter[] parameters, int depth, Variables globalParameters) {
		if (parameters.length > 0) {
			final Long count = this.first ? 1 : parameters[0].parse().asLong();
			if (count != null) {
				final int intCount = count.intValue();
				ArrayList<Long> longs = new ArrayList<Long>(parameters.length);
				for (int i = (this.first ? 0 : 1); i < parameters.length; i++) {
					Long longBuffer = parameters[i].parse().asLong();
					if (longBuffer != null) {
						longs.add(longBuffer);
					}
				}
				if (longs.size() >= intCount) {
					final long[] highest = new long[intCount];
					int filled = 0;
					for (Long longBuffer : longs) {
						int i;
						for (i = 0; i < filled && this.compare(highest[i], longBuffer); i++) {}
						if (i < highest.length) {
							for (int j = Math.min(filled, highest.length - 1); i < j; j--) {
								highest[j] = highest[j - 1];
							}
							highest[i] = longBuffer;
							filled = Math.min(highest.length, filled + 1);
						}
					}
					return new LongParameterType(highest[intCount - 1]);
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
