package com.tahkeh.loginmessage.methods.impl;

import com.tahkeh.loginmessage.methods.EmptyMethod;

public class NullMethod extends EmptyMethod {

	public static final NullMethod INSTANCE = new NullMethod();

	public NullMethod() {
		super("null");
	}

	@Override
	public String call() {
		return null;
	}

}
