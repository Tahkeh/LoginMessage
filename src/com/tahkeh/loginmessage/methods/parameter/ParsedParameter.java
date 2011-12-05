package com.tahkeh.loginmessage.methods.parameter;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.MethodParser;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class ParsedParameter implements Parameter {

	private final MethodParser parser;
	private final OfflinePlayer player;
	private final String event;
	private final String parameterValue;
	private final Variables variable;
	private final int depth;

	public ParsedParameter(final MethodParser parser, final OfflinePlayer player, final String event, final String parameterValue, final Variables variable, final int depth) {
		this.parser = parser;
		this.player = player;
		this.event = event;
		this.parameterValue = parameterValue;
		this.variable = variable;
		this.depth = depth;
	}

	@Override
	public String parse() {
		return this.parser.parseLine(this.player, this.event, this.parameterValue, this.variable, this.depth);
	}

}
