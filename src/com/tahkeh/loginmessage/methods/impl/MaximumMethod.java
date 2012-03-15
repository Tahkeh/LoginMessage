package com.tahkeh.loginmessage.methods.impl;

import com.tahkeh.loginmessage.methods.preset.MinMaxMethod;

public class MaximumMethod extends MinMaxMethod {

	public MaximumMethod(final boolean first) {
		super(first, "max");
	}

	@Override
	protected boolean compare(long nHighest, long tested) {
		return nHighest > tested;
	}
}
