package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.parameter.Parameter;

import de.xzise.EqualCheck;
import de.xzise.MinecraftUtil;

public class IfArithmeticMethod extends IfMethod {

	private final EqualCheck<? super Double> checker;

	public IfArithmeticMethod(final EqualCheck<? super Double> checker) {
		super(2, false);
		this.checker = checker;
	}

	@Override
	protected Boolean match(OfflinePlayer player, String event, Parameter[] preValues) {
		Double a = MinecraftUtil.tryAndGetDouble(preValues[0].parse());
		Double b = MinecraftUtil.tryAndGetDouble(preValues[1].parse());
		if (a != null && b != null) {
			return this.checker.equals(a, b);
		} else {
			return null;
		}
	}

}
