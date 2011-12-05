package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.MethodParser;
import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class PrintPrefixMethod extends OriginalMethod {

	private final MethodParser parser;

	public PrintPrefixMethod(final MethodParser parser) {
		this.parser = parser;
	}

	@Override
	public String call(OfflinePlayer player, String event, Variables globalParameters) {
		return this.parser.getPrefix();
	}

	public PrintPrefixMethod register() {
		super.register(this.parser.getPrefix(), this.parser);
		return this;
	}

	public PrintPrefixMethod unregister() {
		super.unregister(this.parser.getPrefix(), this.parser);
		return this;
	}
}
