package com.tahkeh.loginmessage.methods.preset;

import com.tahkeh.loginmessage.methods.MethodParser;
import com.tahkeh.loginmessage.methods.variables.Variables;

public abstract class DefaultCastedNamedMethod<V extends Variables, C extends V> extends DefaultCastedMethod<V, C> {

	private final String defaultName;

	public DefaultCastedNamedMethod(final String defaultName, final Class<? extends C> variableClass, final int paramCount, final int... paramCounts) {
		super(variableClass, paramCount, paramCounts);
		this.defaultName = defaultName;
	}

	public final DefaultCastedNamedMethod<V, C> register(MethodParser<? extends V> parser) {
		super.register(this.defaultName, parser);
		return this;
	}

	public final DefaultCastedNamedMethod<V, C> unregister(MethodParser<? extends V> parser) {
		super.unregister(this.defaultName, parser);
		return this;
	}

}
