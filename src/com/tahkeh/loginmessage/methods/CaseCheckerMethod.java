package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.EqualCheck;

public class CaseCheckerMethod extends DefaultNamedMethod {

	private final EqualCheck<? super String> checker;

	public CaseCheckerMethod(final EqualCheck<? super String> checker, final String name) {
		super(true, name, -2);
		this.checker = checker;
	}

	@Override
	public String call(OfflinePlayer player, Parameter[] parameters, Variables globalParameters) {
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
		if (parameters.length >= 2) {
			for (int i = 0; i < (parameters.length - 2) / 2; i++) {
				if (this.checker.equals(parameters[0].parse(), parameters[i * 2 + 1].parse())) {
					return parameters[i * 2 + 2].parse();
				}
			}
			if (parameters.length % 2 == 0) {
				return parameters[parameters.length - 1].parse();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
