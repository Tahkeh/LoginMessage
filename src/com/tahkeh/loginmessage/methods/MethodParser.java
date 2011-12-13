package com.tahkeh.loginmessage.methods;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.tahkeh.loginmessage.methods.impl.BarMethod;
import com.tahkeh.loginmessage.methods.impl.CaseCheckerMethod;
import com.tahkeh.loginmessage.methods.impl.ConstantMethod;
import com.tahkeh.loginmessage.methods.impl.IfArithmeticMethod;
import com.tahkeh.loginmessage.methods.impl.IfCheckerMethod;
import com.tahkeh.loginmessage.methods.impl.IfSetMethod;
import com.tahkeh.loginmessage.methods.impl.IndefiniteArticleMethod;
import com.tahkeh.loginmessage.methods.impl.MaximumMethod;
import com.tahkeh.loginmessage.methods.impl.MinimumMethod;
import com.tahkeh.loginmessage.methods.impl.NullMethod;
import com.tahkeh.loginmessage.methods.impl.PrintMethod;
import com.tahkeh.loginmessage.methods.impl.PrintPrefixMethod;
import com.tahkeh.loginmessage.methods.impl.RandomMethod;
import com.tahkeh.loginmessage.methods.impl.RedirectMethod;
import com.tahkeh.loginmessage.methods.parameter.FinalParameter;
import com.tahkeh.loginmessage.methods.parameter.OnceParsedParameter;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.EqualCheck;
import de.xzise.MinecraftUtil;
import de.xzise.XLogger;

public class MethodParser<V extends Variables> {

	public static final int STOPPING_THRESHOLD = 100;
	public static final int WARNING_THRESHOLD = STOPPING_THRESHOLD * 9 / 10;

	private final Map<String, Map<Integer, Method<? super V>>> methods = new HashMap<String, Map<Integer, Method<? super V>>>();
	private final XLogger logger;
	private String prefix = "";

	public MethodParser(final XLogger logger, final String prefix) {
		this.logger = logger;
		this.setPrefix(prefix);
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(final String prefix) {
		if (prefix == null) {
			throw new IllegalArgumentException("Prefix has to be not null.");
		} else if (prefix.contains(" ")) {
			throw new IllegalArgumentException("Prefix mustn't contain spaces.");
		} else if (prefix.contains("(") || prefix.contains(")")) {
			throw new IllegalArgumentException("Prefix mustn't contain brackets.");
		}
		this.prefix = prefix;
	}

	private int[] testParameters(final String name, final int[] paramCount) {
		if (!MinecraftUtil.isSet(name)) {
			throw new IllegalArgumentException("Name has to be set (not null and not empty)!");
		} else if (name.contains(" ")) {
			throw new IllegalArgumentException("Name mustn't contain spaces.");
		} else if (name.contains("(") || name.contains(")")) {
			throw new IllegalArgumentException("Name mustn't contain brackets.");
		} else {
			if (paramCount.length == 0) {
				return new int[] { -1, 0 };
			} else {
				return paramCount;
			}
		}
	}

	/**
	 * Registers a new method.
	 * 
	 * @param name
	 *            New name of the method. No spaces are allowed and without the
	 *            prefix.
	 * @param method
	 *            New method.
	 * @param paramCount
	 *            The number of parameters the method is registered to. There
	 *            could be negative values which allows more than the specified
	 *            absolute value.
	 * @return How many methods were already registered and overwritten.
	 */
	public int registerMethod(final String name, Method<? super V> method, int... paramCount) {
		paramCount = testParameters(name, paramCount);
		// TODO: Case insensitive?
		Map<Integer, Method<? super V>> methods = this.methods.get(name);
		if (methods == null) {
			methods = new HashMap<Integer, Method<? super V>>();
			this.methods.put(name, methods);
		}
		int failCount = 0;
		for (int i : paramCount) {
			if (methods.put(i, method) != null) {
				failCount++;
			}
		}
		return failCount;
	}

	public int unregisterMethod(final String name, int... paramCount) {
		paramCount = testParameters(name, paramCount);
		// TODO: Case insensitive?
		Map<Integer, Method<? super V>> methods = this.methods.get(name);
		int failCount = 0;
		if (methods != null) {
			for (int i : paramCount) {
				if (methods.remove(i) != null) {
					failCount++;
				}
			}
			if (methods.isEmpty()) {
				this.methods.remove(name);
			}
		}
		return failCount;
	}

	/**
	 * <p>
	 * Returns a method with the specified name and parameter count. Negative
	 * parameter counts means the minimum of allowed parameters. If there is no
	 * method with the specified parameter count it will select the method which
	 * can handle the parameter count but they may can handle more or less than
	 * specified.
	 * </p>
	 * <p>
	 * Example: The {@code paramCount} parameter is {@code 3} and there is no
	 * method registered with 3 parameters it will try {@code -3}, {@code -2}
	 * and {@code -1} parameter and selects the first found. Here does the
	 * {@code -3} means that the method can handle 3 or more parameters.
	 * </p>
	 * 
	 * @param name
	 *            name of the method.
	 * @param paramCount
	 *            Number of parameters. Negative parameter count means that the
	 *            method can also allow more than the specified value.
	 * @return a method with the name and parameter count. If there wasn't a
	 *         method found it will return null.
	 */
	public Method<? super V> getMethod(final String name, final int paramCount) {
		// TODO: Case insensitive?
		if (name != null) {
			Map<Integer, Method<? super V>> methods = this.methods.get(name.substring(this.prefix.length()));
			if (methods != null) {
				return getMethod(methods, paramCount);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private static <V extends Variables> Method<? super V> getMethod(Map<Integer, Method<? super V>> methods, int paramCount) {
		if (methods.containsKey(paramCount)) {
			return methods.get(paramCount);
		} else {
			Method<? super V> method = null;
			paramCount = -Math.abs(paramCount);
			while (paramCount < 0 && method == null) {
				method = methods.get(paramCount);
				paramCount++;
			}
			return method;
		}
	}

	public void clearMethods() {
		this.methods.clear();
	}

	public String parseLine(String line, V globalParameters) {
		return this.parseLine(line, globalParameters, 0);
	}

	public String parseLine(String line, V globalParameters, final int depth) {
		int index = 0;
		int start = -1;
		int delim = -1;
		int paramEnd = -1;
		int end = -1;
		int bracketLevel = 0;
		boolean quoted = false;
		while (index <= line.length()) {
			if (index < line.length()) {
				char c = line.charAt(index);
				switch (c) {
				case '\\':
					index++;
					break;
				case '"':
					quoted = !quoted;
					break;
				case '(':
					if (start >= 0) {
						if (!quoted) {
							if (bracketLevel == 0) {
								delim = index;
							}
							bracketLevel++;
						}
					} else {
						start = index;
					}
					break;
				case ')':
					if (!quoted) {
						if (bracketLevel > 0) {
							bracketLevel--;
							if (bracketLevel == 0) {
								end = index;
								paramEnd = index - 1;
							}
						}
					}
					break;
				case ' ':
					if (bracketLevel == 0 && start >= 0) {
						end = index - 1;
						paramEnd = index - 1;
					}
					break;
				default:
					if (start < 0) {
						start = index;
					}
					break;
				}
			} else if (start >= 0) {
				bracketLevel = 0;
				end = index - 1;
				paramEnd = index - 1;
			}

			if (start >= 0 && end >= start) {
				int nameEnd;
				String[] parameters;
				if (delim > start) {
					nameEnd = delim - 1;
					final String parameterText = line.substring(delim + 1, paramEnd + 1);
					if (parameterText.length() > 0) {
						parameters = MinecraftUtil.parseLine(parameterText, ',', '"', '\\', '(', ')', true);
					} else {
						parameters = new String[0];
					}
				} else {
					nameEnd = end;
					parameters = new String[0];
				}
				final String name = line.substring(start, nameEnd + 1);

				final Method<? super V> method;
				final String methodName;
				if (MinecraftUtil.isSet(this.prefix)) {
					final int idx = name.indexOf(this.prefix);
					if (idx > 0) {
						start += idx;
						methodName = name.substring(idx);
					} else if (idx == 0) {
						methodName = name;
					} else {
						methodName = null;
					}
				} else {
					methodName = name;
				}
				if (methodName != null) {
					method = this.getMethod(methodName, parameters.length);
				} else {
					method = null;
				}
				if (method != null) {
					if (depth < STOPPING_THRESHOLD) {
						if (depth >= WARNING_THRESHOLD) {
							this.logger.warning("Deep method call of '" + name + "' at depth " + depth);
						}
						Parameter[] parameterObjects = new Parameter[parameters.length];
						for (int i = 0; i < parameters.length; i++) {
							if (method.isRecursive()) {
								parameterObjects[i] = OnceParsedParameter.create(this, parameters[i], globalParameters, depth + 1);
							} else {
								parameterObjects[i] = new FinalParameter(parameters[i]);
							}
						}
						String replacement = null;
						try {
							replacement = method.call(parameterObjects, globalParameters);
						} catch (Exception e) {
							this.logger.warning("Exception by calling '" + name + "'!", e);
						}
						if (replacement != null) {
							if (method.isRecursive()) {
								replacement = parseLine(replacement, globalParameters, depth + 1);
							}
							line = line.substring(0, start) + replacement + substring(line, end + 1, line.length());
							index += replacement.length() - (end - start) - 1;
						}
					} else {
						this.logger.severe("Didn't called method '" + name + "' at depth " + depth);
					}
				}
				index++;
				end = -1;
				start = -1;
				delim = -1;
			} else {
				index++;
			}
		}

		return MinecraftUtil.isSet(line) ? line : null;
	}

	public void loadDefaults() {
		new PrintMethod(true, "call").register(this);
		new PrintMethod(false, "print").register(this);

		// IfChecker
		this.registerMethod("ifequals", new IfCheckerMethod(EqualCheck.CLASSIC_EQUAL_CHECKER, false), 3, 4);
		this.registerMethod("ifnotequals", new IfCheckerMethod(EqualCheck.CLASSIC_EQUAL_CHECKER, true), 3, 4);
		this.registerMethod("ifequalsignorecase", new IfCheckerMethod(EqualCheck.STRING_IGNORE_CASE_EQUAL_CHECKER, false), 3, 4);
		this.registerMethod("ifnotequalsignorecase", new IfCheckerMethod(EqualCheck.STRING_IGNORE_CASE_EQUAL_CHECKER, true), 3, 4);
		this.registerMethod("ifset", new IfSetMethod(false), 2, 3);
		this.registerMethod("ifnotset", new IfSetMethod(true), 2, 3);

		new CaseCheckerMethod(EqualCheck.CLASSIC_EQUAL_CHECKER, "caseequals").register(this);

		this.registerMethod("ifgreaterequals", new IfArithmeticMethod(EqualCheck.GREATER_EQUAL_CHECKER), 3, 4);
		this.registerMethod("ifgreater", new IfArithmeticMethod(EqualCheck.GREATER_CHECKER), 3, 4);
		this.registerMethod("iflower", new IfArithmeticMethod(EqualCheck.LOWER_CHECKER), 3, 4);
		this.registerMethod("iflowerequals", new IfArithmeticMethod(EqualCheck.LOWER_EQUAL_CHECKER), 3, 4);

		this.registerMethod("random", new RandomMethod(), -1);

		new NullMethod().register(this);

		new IndefiniteArticleMethod().register(this);
		new ConstantMethod("", "sp").register(this);
		new MaximumMethod(true).register(this);
		new MaximumMethod(false).register(this);
		new MinimumMethod(true).register(this);
		new MinimumMethod(false).register(this);
		new PrintPrefixMethod(this).register();
		new BarMethod().register(this);
	}

	private static String substring(String input, int start, int end) {
		if (end < start) {
			return "";
		} else {
			return input.substring(start, end);
		}
	}

	public void createRedirected(String name, String redirected, int... paramCounts) {
		final Collection<Integer> paramCountsSet;
		Map<Integer, Method<? super V>> methods = this.methods.get(redirected);
		if (paramCounts.length == 0) {
			if (methods != null) {
				paramCountsSet = methods.keySet();
			} else {
				paramCountsSet = new HashSet<Integer>(0);
			}
		} else {
			paramCountsSet = new HashSet<Integer>(paramCounts.length);
			for (int paramCount : paramCounts) {
				paramCountsSet.add(paramCount);
			}
		}
		for (int paramCount : paramCounts) {
			Method<? super V> redirectedMethod = getMethod(methods, paramCount);
			if (redirectedMethod != null) {
				this.registerMethod(name, new RedirectMethod<V>(redirectedMethod), paramCount);
			}
		}
	}

	public boolean createRedirected(String name, String redirected, int paramCount) {
		Method<? super V> redirectedMethod = this.getMethod(redirected, paramCount);
		if (redirectedMethod != null) {
			this.registerMethod(name, new RedirectMethod<V>(redirectedMethod), paramCount);
			return true;
		} else {
			return false;
		}
	}

	public static class RedirectedElement {
		public final int paramCount;
		public final String name;
		public final String redirected;

		public RedirectedElement(String name, String redirected, int paramCount) {
			this.name = name;
			this.redirected = redirected;
			this.paramCount = paramCount;
		}
	}

	public void createRedirected(List<RedirectedElement> elements) {
		List<RedirectedElement> buffer = null;
		List<RedirectedElement> output = new ArrayList<RedirectedElement>(elements);
		int startSize;
		do {
			startSize = output.size();
			buffer = new ArrayList<RedirectedElement>(startSize);
			for (RedirectedElement redirectedElement : output) {
				if (!this.createRedirected(redirectedElement.name, redirectedElement.redirected, redirectedElement.paramCount)) {
					buffer.add(redirectedElement);
				}
			}
			output = buffer;
		} while (startSize > buffer.size());
		elements.clear();
		elements.addAll(output);
	}
}