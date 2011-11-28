package com.tahkeh.loginmessage.methods.parameter;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.MethodParser;
import com.tahkeh.loginmessage.methods.variables.DefaultVariables;

public class OnceParsedParameter extends ParsedParameter {

	private String parsedParameter = null;
	private boolean parsed = false;

	public OnceParsedParameter(final MethodParser parser, final OfflinePlayer player, final String event, final String parameterValue, final DefaultVariables variable, final int depth) {
		super(parser, player, event, parameterValue, variable, depth);
	}

	@Override
	public String parse() {
		if (!this.parsed) {
			this.parsedParameter = super.parse();
			this.parsed = true;
		}
		return this.parsedParameter;
	}
}
