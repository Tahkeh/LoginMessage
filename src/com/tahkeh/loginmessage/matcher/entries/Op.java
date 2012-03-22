package com.tahkeh.loginmessage.matcher.entries;

import org.bukkit.OfflinePlayer;

public class Op implements Entry {

	private final boolean positive;

	public Op(boolean positive) {
		this.positive = positive;
	}

	@Override
	public boolean isPositive() {
		return this.positive;
	}

	@Override
	public boolean match(OfflinePlayer player) {
		return player.isOp();
	}

}
