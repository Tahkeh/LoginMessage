package com.tahkeh.loginmessage.methods.preset;

import com.tahkeh.loginmessage.methods.MethodParser;
import com.tahkeh.loginmessage.methods.variables.Variables;

/**
 * Extended default implementation which adds support for a default method name.
 */
public abstract class DefaultNamedMethod<V extends Variables> extends DefaultMethod<V> {

	private final String defaultName;

	public DefaultNamedMethod(final String defaultName, final int paramCount, final int... paramCounts) {
		super(paramCount, paramCounts);
		this.defaultName = defaultName;
	}

	public final DefaultNamedMethod<V> register(MethodParser<? extends V> parser) {
		super.register(this.defaultName, parser);
		return this;
	}

	public final DefaultNamedMethod<V> unregister(MethodParser<? extends V> parser) {
		super.unregister(this.defaultName, parser);
		return this;
	}
}
