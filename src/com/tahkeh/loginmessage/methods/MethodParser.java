package com.tahkeh.loginmessage.methods;


import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

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
	 *            The number of parameters the method is registered to.
	 * @return How many methods were already registered.
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
	 * Returns a method with the specified name and parameter count. If there is
	 * no method with the specified parameter count it will select the method
	 * with the parameter count of <code>-1</code> if the chosen parameter count
	 * is positive. If the parameter count is lower equals zero and there is no
	 * method registered with the parameter count it will return the method with
	 * no parameters.
	 * 
	 * @param name
	 *            name of the method.
	 * @param paramCount
	 *            Number of parameters. If <code>-1</code> the method allow all
	 *            parameter counts.
	 * @return a method with the name and parameter count. If there wasn't a
	 *         method found it will return null.
	 */
	public Method getMethod(String name, int paramCount) {
		// TODO: Case insensitive?
		Map<Integer, Method> methods = this.methods.get(name);
		if (methods != null) {
			if (methods.containsKey(paramCount)) {
				return methods.get(paramCount);
			} else {
				return methods.get(paramCount > 0 ? -1 : 0);
			}
		} else {
			return null;
		}
	}

	public void clearMethods() {
		this.methods.clear();
	}

	public String parseLine(Player player, String event, String line) {
		return this.parseLine(player, event, line, 0);
	}

	private String parseLine(Player player, String event, String line, final int depth) {
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
								parameters[i] = parseLine(player, event, parameters[i], depth + 1);
							}
						}
						String replacement = method.call(player, event, parameters);
						if (replacement != null) {
							if (method.recursive()) {
								replacement = parseLine(player, event, replacement, depth + 1);
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

	public void loadDefaults() {
		this.registerMethod("onlist", new OnlistMethod(this.logger), 0, 2, 3);
		this.registerMethod("call", new PrintMethod(true), -1);
		this.registerMethod("print", new PrintMethod(false), -1);
		this.registerMethod("ifequals", new IfEqualsMethod(false), 3, 4);
		this.registerMethod("ifnotequals", new IfEqualsMethod(true), 3, 4);
		this.registerMethod("ifset", new IfSetMethod(false), 2, 3);
		this.registerMethod("ifnotset", new IfSetMethod(true), 2, 3);
		this.registerMethod("ifequalsignorecase", new IfEqualsIgnoreCaseMethod(false), 3, 4);
		this.registerMethod("ifnotequalsignorecase", new IfEqualsIgnoreCaseMethod(true), 3, 4);
	}

	private static String substring(String input, int start, int end) {
		if (end < start) {
			return "";
		} else {
			return input.substring(start, end);
		}
	}

	public static void createRedirected(MethodParser parser, String name, String redirected, int... paramCounts) {
		for (int paramCount : paramCounts) {
			Method redirectedMethod = parser.getMethod(redirected, paramCount);
			if (redirectedMethod != null) {
				parser.registerMethod(name, new RedirectMethod(redirectedMethod), paramCount);
			}
        }
	}
}
