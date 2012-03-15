package com.tahkeh.loginmessage.methods.impl;

import com.tahkeh.loginmessage.methods.preset.MinMaxMethod;

public class MinimumMethod extends MinMaxMethod {

	public MinimumMethod(final boolean first) {
		super(first, "min");
	}

	@Override
	protected boolean compare(long nLowest, long tested) {
		return nLowest < tested;
	}

}
