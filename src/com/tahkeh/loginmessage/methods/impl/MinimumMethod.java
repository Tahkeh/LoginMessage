package com.tahkeh.loginmessage.methods.impl;

import com.tahkeh.loginmessage.methods.MinMaxMethod;

public class MinimumMethod extends MinMaxMethod {

	public MinimumMethod(final boolean first) {
		super(first, "min");
	}

	@Override
	protected boolean compare(int nLowest, int tested) {
		return nLowest < tested;
	}

}
