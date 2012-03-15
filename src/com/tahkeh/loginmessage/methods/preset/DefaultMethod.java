package com.tahkeh.loginmessage.methods.preset;

import com.tahkeh.loginmessage.methods.Method;
import com.tahkeh.loginmessage.methods.MethodParser;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.MinecraftUtil;

/**
 * Default method implementation with the allowed parameter counts.
 */
public abstract class DefaultMethod<V extends Variables> implements Method<V> {

	private final int[] paramCounts;

	public DefaultMethod(final int paramCount, final int... paramCounts) {
		this.paramCounts = MinecraftUtil.concat(paramCount, paramCounts);
	}

	public final int[] getParamCounts() {
		return this.paramCounts.clone();
	}

	public final DefaultMethod<V> register(String name, MethodParser<? extends V> parser) {
		parser.registerMethod(name, this, this.paramCounts);
		return this;
	}

	public final DefaultMethod<V> unregister(String name, MethodParser<? extends V> parser) {
		parser.unregisterMethod(name, this.paramCounts);
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

	// Maybe future parameter type: { first, second, "third" }
	// public static String[] parseAsArray(String parameter) {
	// if (MinecraftUtil.isSet(parameter)) {
	//
	// } else {
	// return null;
	// }
	// }

	/**
	 * Try to read the parameter as an integer. This methods uses all features
	 * from {@link #parseAsIntegerFixed(String)} but also allows flexible
	 * radixes by using a underscore sign. For example the string
	 * <code>20_3</code> returns the decimal value 6. The radix is
	 * <code>3</code> and the value is <code>20</code>. The radix will be parsed
	 * with {@link #parseAsIntegerFixed(String)} and the value with
	 * {@link Integer#parseInt(String)}.
	 * 
	 * @param parameter
	 *            parameter value.
	 * @return The parameter parsed as an integer. If it isn't a valid value it
	 *         returns <code>null</code>.
	 * @see DefaultMethod#parseAsLongFixed(String)
	 */
	public static Long parseAsLong(final String parameter) {
		if (MinecraftUtil.isSet(parameter)) {
			Long fixedParse = parseAsLongFixed(parameter);
			if (fixedParse != null) {
				return fixedParse;
			} else {
				final String[] s = parameter.split("_");
				if (s.length == 2) {
					Long radix = parseAsLongFixed(s[1]);
					return radix != null ? MinecraftUtil.tryAndGetLong(s[0], radix.intValue()) : null;
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
	}

	/**
	 * Try to read the parameter as a long. There are several configurations
	 * possible:
	 * <ul>
	 * <li>If the parameter's length is one it try to read it as a hexadecimal
	 * value.</li>
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
	 * @return The parameter parsed as a long. If it isn't a valid value it
	 *         returns <code>null</code>.
	 * @see DefaultMethod#parseAsLong(String)
	 */
	public static Long parseAsLongFixed(final String parameter) {
		if (MinecraftUtil.isSet(parameter)) {
			if (parameter.length() == 1) {
				return MinecraftUtil.tryAndGetLong(parameter, 16);
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
					if (parameter.charAt(start) == 'x') {
						radix = 16;
						start++; // Skip 'x'
					} else if (parameter.charAt(start) == 'b') {
						radix = 2;
						start++; // Skip 'b'
					} else {
						radix = 8;
					}
				} else {
					radix = 10;
				}
				try {
					final long value = Long.parseLong(parameter.substring(start), radix);
					return positive ? value : -value;
				} catch (NumberFormatException e) {
					return null;
				}
			}
		} else {
			return null;
		}
	}
}
