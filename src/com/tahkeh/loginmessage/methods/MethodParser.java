package com.tahkeh.loginmessage.methods;

import java.util.HashMap;
import java.util.Map;

import de.xzise.MinecraftUtil;
import de.xzise.XLogger;

public class MethodParser {

	public static final int WARNING_THRESHOLD = 1;
	public static final int STOPPING_THRESHOLD = 2;

	private final Map<String, Method> methods = new HashMap<String, Method>();
	private final XLogger logger;

	public MethodParser(XLogger logger) {
		this.registerMethod("onlist", new OnlistMethod(logger));
		this.registerMethod("print", new Print());

		this.logger = logger;
	}

	/**
	 * Registers a new method.
	 * 
	 * @param name
	 *            New name of the method. No spaces are allowed.
	 * @param method
	 *            New method.
	 * @return If there wasn't a method registered with this name.
	 */
	public boolean registerMethod(String name, Method method) {
		if (name.contains(" ")) {
			throw new IllegalArgumentException("Name shouldn't contain spaces.");
		} else {
			// TODO: Case insensitive?
			return this.methods.put(name, method) == null;
		}
	}

	public String parseLine(String line) {
		return this.parseLine(line, 0);
	}

	private String parseLine(String line, int depth) {
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
						bracketLevel--;
						if (bracketLevel == 0) {
							end = index;
							paramEnd = index - 1;
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

			if (end > start) {
				int nameEnd;
				String[] parameters;
				if (delim > start) {
					nameEnd = delim - 1;
					String parameterText = line.substring(delim + 1, paramEnd + 1);
					if (parameterText.length() > 0) {
						parameters = MinecraftUtil.parseLine(parameterText, ',');
					} else {
						parameters = new String[0];
					}
				} else {
					nameEnd = end;
					parameters = new String[0];
				}
				String name = line.substring(start, nameEnd + 1);

				Method method = this.methods.get(name);
				if (method != null) {
					if (depth < STOPPING_THRESHOLD) {
						if (depth >= WARNING_THRESHOLD) {
							this.logger.warning("Deep method call of '" + name + "' at depth " + depth);
						}
						String replacement = method.call(parameters);
						if (replacement != null) {
							if (method.recursive()) {
								replacement = parseLine(replacement, depth + 1);
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

	private static String substring(String input, int start, int end) {
		if (end < start) {
			return "";
		} else {
			return input.substring(start, end);
		}
	}
}
