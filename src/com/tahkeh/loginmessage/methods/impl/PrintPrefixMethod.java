package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.DefaultMethod;
import com.tahkeh.loginmessage.methods.MethodParser;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class PrintPrefixMethod extends DefaultMethod {

	private final MethodParser parser;

	public PrintPrefixMethod(final MethodParser parser) {
		super(false, 0);
		this.parser = parser;
	}

	@Override
	public String call(OfflinePlayer player, String event, Parameter[] parameters, Variables globalParameters) {
		if (parameters.length == 0) {
			return this.parser.getPrefix();
		} else {
			return null;
		}
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
