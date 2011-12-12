package com.tahkeh.loginmessage.methods.variables;

public class PlayerNotFoundVariables extends Variables {

	public final String targetName;

	public PlayerNotFoundVariables(final String targetName) {
		super(false, "playernotfound");
		this.targetName = targetName;
	}

}
