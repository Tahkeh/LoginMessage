package com.tahkeh.loginmessage.methods.impl;

import java.util.ArrayList;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.DefaultMethod;
import com.tahkeh.loginmessage.methods.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

public abstract class MinMaxMethod extends DefaultNamedMethod {

	private final boolean first;

	protected MinMaxMethod(final boolean first, final String defaultName) {
		super(true, first ? defaultName : "n" + defaultName, first ? -1 : -2);
		this.first = first;
	}

	protected abstract boolean compare(final int nHighLowest, final int tested);

	@Override
	public String call(OfflinePlayer player, String event, Parameter[] parameters, Variables globalParameters) {
		if (parameters.length > 0) {
			final Integer count = this.first ? 1 : DefaultMethod.parseAsInteger(parameters[0].parse());
			if (count != null) {
				ArrayList<Integer> integers = new ArrayList<Integer>(parameters.length);
				for (int i = (this.first ? 0 : 1); i < parameters.length; i++) {
					Integer integer = DefaultMethod.parseAsInteger(parameters[i].parse());
					if (integer != null) {
						integers.add(integer);
					}
				}
				if (integers.size() >= count) {
					final int[] highest = new int[count];
					int filled = 0;
					for (Integer integer : integers) {
						int i;
						for (i = 0; i < filled && this.compare(highest[i], integer); i++) {}
						if (i < highest.length) {
							for (int j = Math.min(filled, highest.length - 1); i < j; j--) {
								highest[j] = highest[j - 1];
							}
							highest[i] = integer;
							filled = Math.min(highest.length, filled + 1);
						}
					}
					return Integer.toString(highest[count - 1]);
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
