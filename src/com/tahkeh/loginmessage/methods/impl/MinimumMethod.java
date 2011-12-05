package com.tahkeh.loginmessage.methods.impl;

public class MinimumMethod extends MinMaxMethod {

	public MinimumMethod(final boolean first) {
		super(first);
	}

	@Override
	protected boolean compare(int nLowest, int tested) {
		return nLowest < tested;
	}

}
