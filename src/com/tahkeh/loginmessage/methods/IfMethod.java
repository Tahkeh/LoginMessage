package com.tahkeh.loginmessage.methods;

import java.util.Arrays;

import org.bukkit.entity.Player;

public abstract class IfMethod implements Method {

	private final int preValueCount;
	private final boolean inverted;

	protected IfMethod(final int preValueCount, final boolean inverted) {
		this.preValueCount = Math.max(0, preValueCount);
		this.inverted = inverted;
	}

	@Override
	public final String call(Player player, String event, String... parameters) {
		String match = "";
		String noMatch = "";
		switch (parameters.length - this.preValueCount) {
		case 2:
			noMatch = parameters[this.preValueCount + 1];
		case 1:
			match = parameters[this.preValueCount];
			break;
		default:
			return null;
		}
		return this.match(player, event, Arrays.copyOf(parameters, this.preValueCount)) != this.inverted ? match : noMatch;
	}

	protected abstract boolean match(Player player, String event, String... preValues);

	@Override
	public boolean recursive() {
		return true;
	}

}
