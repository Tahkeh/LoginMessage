package com.tahkeh.loginmessage.methods;

/**
 * Extended default implementation which adds support for a default method name.
 */
public abstract class DefaultNamedMethod extends DefaultMethod {

	private final String defaultName;

	public DefaultNamedMethod(final boolean recursive, final String defaultName, final int paramCount, final int... paramCounts) {
		super(recursive, paramCount, paramCounts);
		this.defaultName = defaultName;
	}

	public final DefaultNamedMethod register(MethodParser parser) {
		super.register(this.defaultName, parser);
		return this;
	}

	public final DefaultNamedMethod unregister(MethodParser parser) {
		super.unregister(this.defaultName, parser);
		return this;
	}
}
