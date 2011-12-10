package com.tahkeh.loginmessage.methods.variables;

public class KickVariables extends Variables {

	public final String reason;

	public KickVariables(final String reason) {
		super(true, "kick");
		this.reason = reason;
	}
}
