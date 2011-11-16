package com.tahkeh.loginmessage.methods;

import java.util.Comparator;

import org.bukkit.OfflinePlayer;

import de.xzise.EqualCheck;

public class IfCheckerMethod extends IfMethod {

	private final EqualCheck<? super String> checker;

	public IfCheckerMethod(EqualCheck<? super String> checker, boolean inverted) {
		super(2, inverted);
		this.checker = checker;
	}

	public IfCheckerMethod(Comparator<String> checker, boolean inverted) {
		this((EqualCheck<String>) new EqualCheck.ComparatorEqualChecker<String>(checker), inverted);
	}

	@Override
	protected Boolean match(OfflinePlayer player, String event, String... preValues) {
		return this.checker.equals(preValues[0], preValues[1]);
	}
}
