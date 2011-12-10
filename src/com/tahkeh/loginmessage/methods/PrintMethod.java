package com.tahkeh.loginmessage.methods;


import java.util.Iterator;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.MinecraftUtil;
import de.xzise.collections.ArrayIterator;

/**
 * Returns all parameters.
 */
public class PrintMethod extends DefaultNamedMethod {

	public PrintMethod(final boolean isRecursive, final String defaultName) {
		super(isRecursive, defaultName, -1);
	}

	@Override
	public String call(OfflinePlayer player, Parameter[] parameters, Variables globalParameters) {
		StringBuilder builder = new StringBuilder();
		for (Iterator<Parameter> parameterItr = new ArrayIterator<Parameter>(parameters); parameterItr.hasNext();) {
			String next = parameterItr.next().parse();
			if (MinecraftUtil.isSet(next)) {
				builder.append(next);
				if (parameterItr.hasNext()) {
					builder.append(" ");
				}
			}
		}
		return builder.toString();
	}

}
