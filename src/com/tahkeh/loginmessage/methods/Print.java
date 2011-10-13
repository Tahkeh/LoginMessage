package com.tahkeh.loginmessage.methods;

import java.util.Iterator;

import de.xzise.MinecraftUtil;
import de.xzise.collections.ArrayIterator;

public class Print implements Method {

	@Override
	public String call(String... parameters) {
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
	    return true;
    }

}
