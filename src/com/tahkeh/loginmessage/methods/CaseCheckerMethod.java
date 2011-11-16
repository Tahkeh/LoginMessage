package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;

import de.xzise.EqualCheck;

public class CaseCheckerMethod implements Method {

	private final EqualCheck<? super String> checker;

	public CaseCheckerMethod(final EqualCheck<? super String> checker) {
		this.checker = checker;
	}

	@Override
	public String call(OfflinePlayer player, String event, String... parameters) {
		//@formatter:off
	    /*
	     * %case(
	     * 0    <value>,
	     * 1    <cond 1>,
	     * 2    <call 1>,
	     * 3    <cond 2>,
	     * 4    <call 2>,
	     *   …,
	     * 2n+1 <cond n>,
	     * 2n+2 <call n>,
	     * 2n+3 [default call]
	     * )
	     * 
	     * cond idx = section number * 2 + 1
	     * call idx = cond idx + 1
	     * default call idx = (cond idx + 1)
	     * section count = (parameter count - 2) / 2
	     */
		//@formatter:on
		System.out.println("case checker w/ " + parameters[0]);
		if (parameters.length >= 2) {
			for (int i = 0; i < (parameters.length - 2) / 2; i++) {
				if (this.checker.equals(parameters[0], parameters[i * 2 + 1])) {
					return parameters[i * 2 + 2];
				}
			}
			if (parameters.length % 2 == 0) {
				return parameters[parameters.length - 1];
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public boolean recursive() {
		return true;
	}

}
