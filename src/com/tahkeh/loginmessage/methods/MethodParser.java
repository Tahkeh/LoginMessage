package com.tahkeh.loginmessage.methods;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bukkit.OfflinePlayer;

import de.xzise.EqualCheck;
import de.xzise.MinecraftUtil;
import de.xzise.XLogger;

public class MethodParser {

	public static final int STOPPING_THRESHOLD = 100;
	public static final int WARNING_THRESHOLD = STOPPING_THRESHOLD * 9 / 10;

	private final Map<String, Map<Integer, Method>> methods = new HashMap<String, Map<Integer, Method>>();
	private final XLogger logger;

	public MethodParser(XLogger logger) {
		this.logger = logger;
	}

	/**
	 * Registers a new method.
	 * 
	 * @param name
	 *            New name of the method. No spaces are allowed.
	 * @param method
	 *            New method.
	 * @param paramCount
	 *            The number of parameters the method is registered to. There
	 *            could be negative values which allows more than the specified
	 *            absolute value.
	 * @return How many methods were already registered and overwritten.
	 */
	public int registerMethod(String name, Method method, int... paramCount) {
		if (name.contains(" ")) {
			throw new IllegalArgumentException("Name shouldn't contain spaces.");
		} else {
			if (paramCount.length == 0) {
				paramCount = new int[] { -1, 0 };
			}
			// TODO: Case insensitive?
			Map<Integer, Method> methods = this.methods.get(name);
			if (methods == null) {
				methods = new HashMap<Integer, Method>();
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
	public Method getMethod(String name, int paramCount) {
		// TODO: Case insensitive?
		Map<Integer, Method> methods = this.methods.get(name);
		if (methods != null) {
			return getMethod(methods, paramCount);
		} else {
			return null;
		}
	}

	private static Method getMethod(Map<Integer, Method> methods, int paramCount) {
		if (methods.containsKey(paramCount)) {
			return methods.get(paramCount);
		} else {
			Method method = null;
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

	public String parseLine(OfflinePlayer p, String event, String line) {
		return this.parseLine(p, event, line, 0);
	}

	private String parseLine(OfflinePlayer p, String event, String line, final int depth) {
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
					String parameterText = line.substring(delim + 1, paramEnd + 1);
					if (parameterText.length() > 0) {
						parameters = MinecraftUtil.parseLine(parameterText, ',', '"', '\\', '(', ')', true);
					} else {
						parameters = new String[0];
					}
				} else {
					nameEnd = end;
					parameters = new String[0];
				}
				String name = line.substring(start, nameEnd + 1);

				final Method method = this.getMethod(name, parameters.length);
				if (method != null) {
					if (depth < STOPPING_THRESHOLD) {
						if (depth >= WARNING_THRESHOLD) {
							this.logger.warning("Deep method call of '" + name + "' at depth " + depth);
						}
						if (method.recursive()) {
							for (int i = 0; i < parameters.length; i++) {
								parameters[i] = parseLine(p, event, parameters[i], depth + 1);
							}
						}
						String replacement = method.call(p, event, parameters);
						if (replacement != null) {
							if (method.recursive()) {
								replacement = parseLine(p, event, replacement, depth + 1);
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
			} else {
				index++;
			}
		}

		return line;
	}

	public void loadDefaults(final String prefix) {
		this.registerMethod(prefix + "onlist", new OnlistMethod(this.logger), 0, 2, 3);
		this.registerMethod(prefix + "call", new PrintMethod(true), -1);
		this.registerMethod(prefix + "print", new PrintMethod(false), -1);

		// IfChecker
		this.registerMethod(prefix + "ifequals", new IfCheckerMethod(EqualCheck.CLASSIC_EQUAL_CHECKER, false), 3, 4);
		this.registerMethod(prefix + "ifnotequals", new IfCheckerMethod(EqualCheck.CLASSIC_EQUAL_CHECKER, true), 3, 4);
		this.registerMethod(prefix + "ifequalsignorecase", new IfCheckerMethod(EqualCheck.STRING_IGNORE_CASE_EQUAL_CHECKER, false), 3, 4);
		this.registerMethod(prefix + "ifnotequalsignorecase", new IfCheckerMethod(EqualCheck.STRING_IGNORE_CASE_EQUAL_CHECKER, true), 3, 4);
		this.registerMethod(prefix + "ifset", new IfSetMethod(false), 2, 3);
		this.registerMethod(prefix + "ifnotset", new IfSetMethod(true), 2, 3);

		this.registerMethod(prefix + "caseequals", new CaseCheckerMethod(EqualCheck.CLASSIC_EQUAL_CHECKER), -2);

		this.registerMethod(prefix + "ifgreaterequals", new IfArithmeticMethod(EqualCheck.GREATER_EQUAL_CHECKER), 3, 4);
		this.registerMethod(prefix + "ifgreater", new IfArithmeticMethod(EqualCheck.GREATER_CHECKER), 3, 4);
		this.registerMethod(prefix + "iflower", new IfArithmeticMethod(EqualCheck.LOWER_CHECKER), 3, 4);
		this.registerMethod(prefix + "iflowerequals", new IfArithmeticMethod(EqualCheck.LOWER_EQUAL_CHECKER), 3, 4);

		this.registerMethod(prefix + "random", new RandomMethod(), -1);
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
		Map<Integer, Method> methods = this.methods.get(redirected);
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
			Method redirectedMethod = getMethod(methods, paramCount);
			if (redirectedMethod != null) {
				this.registerMethod(name, new RedirectMethod(redirectedMethod), paramCount);
			}
		}
	}

	public boolean createRedirected(String name, String redirected, int paramCount) {
		Method redirectedMethod = this.getMethod(redirected, paramCount);
		if (redirectedMethod != null) {
			this.registerMethod(name, new RedirectMethod(redirectedMethod), paramCount);
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
		} while (startSize >= buffer.size());
		elements.clear();
		elements.addAll(output);
	}
}