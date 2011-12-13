package com.tahkeh.loginmessage.methods;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.variables.Variables;

public abstract class BooleanMethod<V extends Variables> extends CaseMethod<V> {

	private final Message message;

	public BooleanMethod(final Message message, final String defaultName) {
		super(defaultName);
		this.message = message;
	}

	@Override
	protected final String call(V globalParameters) {
		Boolean bool = this.getBoolean(globalParameters);
		if (bool == null) {
			return null;
		} else {
			return this.message.booleanToName(bool);
		}
	}

	protected abstract Boolean getBoolean(V globalParameters);
}
