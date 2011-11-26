package com.tahkeh.loginmessage.methods;

import de.xzise.MinecraftUtil;

/**
 * Default method implementation which implements {@link Method#recursive()}.
 */
public abstract class DefaultMethod implements Method {

	private final boolean recursive;
	private final int[] paramCounts;

	public DefaultMethod(final boolean recursive, final int paramCount, final int... paramCounts) {
		this.recursive = recursive;
		this.paramCounts = MinecraftUtil.concat(paramCount, paramCounts);
	}

	public final int[] getParamCounts() {
		return this.paramCounts.clone();
	}

	@Override
	public final boolean recursive() {
		return this.recursive;
	}

	public final DefaultMethod register(String name, MethodParser parser) {
		parser.registerMethod(name, this, this.paramCounts);
		return this;
	}

	/**
	 * Try to read the parameter as a boolean.
	 * 
	 * @param parameter
	 *            parameter value.
	 * @return <code>true</code> if the parameter is <code>true</code> or
	 *         <code>yes</code> and <code>false</code> if the parameter is
	 *         <code>false</code> or <code>no</code>. In all other cases it
	 *         returns <code>null</code>.
	 */
	public static Boolean parseAsBoolean(String parameter) {
		if (parameter != null) {
			if (parameter.equalsIgnoreCase("true") || parameter.equalsIgnoreCase("yes")) {
				return true;
			} else if (parameter.equalsIgnoreCase("false") || parameter.equalsIgnoreCase("no")) {
				return false;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Try to read the parameter as an integer. There are several configurations
	 * possible:
	 * <ul>
	 * <li>If the parameter's length is one it try to read it as a hexadecimal
	 * value.</li>
	 * <li>If the number is formated like <code>20_3</code> the radix is 3 and
	 * the value is 20. So it will return the decimal value 6.</li>
	 * <li>If the number is formated like <code>0xF</code> it is the hexadecimal
	 * value F and the decimal value 15.</li>
	 * <li>If the number is formated like <code>0b10</code> it is the binary
	 * value 10 and the decimal value 2.</li>
	 * <li>If the number is formated like <code>010</code> it is the octal value
	 * 10 and the decimal value 8.</li>
	 * <li>In all other cases it interprets it as a decimal value.</li>
	 * </ul>
	 * 
	 * @param parameter
	 *            parameter value.
	 * @return The parameter parsed as an integer. If it isn't a valid value it
	 *         returns <code>null</code>.
	 */
	public static Integer parseAsInteger(String parameter) {
		if (MinecraftUtil.isSet(parameter)) {
			if (parameter.length() == 1) {
				return MinecraftUtil.tryAndGetInteger(parameter, 16);
			} else {
				String[] s = parameter.split("_");
				if (s.length == 2) {
					try {
						return Integer.parseInt(s[0], Integer.parseInt(s[1]));
					} catch (NumberFormatException e) {
						return null;
					}
				} else {
					final int radix;
					boolean positive = true;
					int start = 0;
					switch (parameter.charAt(start)) {
					case '-':
						positive = false;
					case '+':
						start++;
						break;
					}
					if (parameter.charAt(start) == '0') {
						start++; // Skip '0'
						if (parameter.charAt(start + 1) == 'x') {
							radix = 16;
							start++; // Skip 'x'
						} else if (parameter.charAt(start + 1) == 'b') {
							radix = 2;
							start++; // Skip 'b'
						} else {
							radix = 8;
						}
					} else {
						radix = 10;
					}
					try {
						final int value = Integer.parseInt(parameter.substring(start), radix);
						return positive ? value : -value;
					} catch (NumberFormatException e) {
						return null;
					}
				}
			}
		} else {
			return null;
		}
	}
}
