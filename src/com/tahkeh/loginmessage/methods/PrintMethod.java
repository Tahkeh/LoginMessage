package com.tahkeh.loginmessage.methods;


import java.util.Iterator;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.variables.DefaultVariables;

import de.xzise.MinecraftUtil;
import de.xzise.collections.ArrayIterator;

/**
 * Returns all parameters.
 */
public class PrintMethod extends DefaultMethod {

	public PrintMethod(final boolean isRecursive) {
		super(isRecursive, -1);
	}

	@Override
	public String call(OfflinePlayer player, String event, String[] parameters, DefaultVariables globalParameters) {
		StringBuilder builder = new StringBuilder();
		for (Iterator<String> stringItr = new ArrayIterator<String>(parameters); stringItr.hasNext();) {
			String next = stringItr.next();
			if (MinecraftUtil.isSet(next)) {
				builder.append(next);
				if (stringItr.hasNext()) {
					builder.append(" ");
				}
			}
		}
		return builder.toString();
	}

}
