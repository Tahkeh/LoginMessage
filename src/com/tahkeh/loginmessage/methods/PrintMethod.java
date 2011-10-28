package com.tahkeh.loginmessage.methods;


import java.util.Iterator;

import org.bukkit.entity.Player;

import de.xzise.MinecraftUtil;
import de.xzise.collections.ArrayIterator;

/**
 * Returns all parameters.
 */
public class PrintMethod implements Method {

	private final boolean isRecursive;

	public PrintMethod(final boolean isRecursive) {
		this.isRecursive = isRecursive;
	}

	@Override
	public String call(Player player, String event, String... parameters) {
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

	@Override
	public boolean recursive() {
		return isRecursive;
	}

}
