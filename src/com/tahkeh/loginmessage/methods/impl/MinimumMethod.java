package com.tahkeh.loginmessage.methods.impl;

public class MinimumMethod extends MinMaxMethod {

	public MinimumMethod(final boolean first) {
		super(first, "min");
	}

	@Override
	protected boolean compare(int nLowest, int tested) {
		return nLowest < tested;
	}

}
