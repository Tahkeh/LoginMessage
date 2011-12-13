package com.tahkeh.loginmessage.methods.impl;

import java.util.Arrays;

import org.bukkit.ChatColor;

import com.tahkeh.loginmessage.methods.DefaultMethod;
import com.tahkeh.loginmessage.methods.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class BarMethod extends DefaultNamedMethod<Variables> {

	public BarMethod() {
		super(true, "bar", 2, 3, 4, 5, 6, 7);
	}

	public static String getBar(int value, int total, int width, char c) {
		return getBar(Math.round((value / (float) total) * width), c);
	}

	public static String getBar(int count, char c) {
		char[] chars = new char[count];
		Arrays.fill(chars, c);
		return new String(chars);
	}

	public static Integer tryParseOptionalAsInteger(final Parameter[] parameters, final int index, final int def) {
		return parameters.length > index ? DefaultMethod.parseAsInteger(parameters[index].parse()) : def;
	}

	public static String tryParseOptionalAsString(final Parameter[] parameters, final int index, final String def) {
		return parameters.length > index ? parameters[index].parse() : def;
	}

	@Override
	public String call(Parameter[] parameters, Variables globalParameters) {
		if (parameters.length >= 2 && parameters.length <= 7) {
			final Integer value = DefaultMethod.parseAsInteger(parameters[0].parse());
			if (value == null) {
				return null;
			}
			final Integer maximum = DefaultMethod.parseAsInteger(parameters[1].parse());
			if (maximum == null) {
				return null;
			}
			final Integer width = tryParseOptionalAsInteger(parameters, 2, 100);
			final Integer leftColorCode = tryParseOptionalAsInteger(parameters, 3, ChatColor.GREEN.getCode());
			if (leftColorCode == null) {
				return null;
			}
			final Integer rightColorCode = tryParseOptionalAsInteger(parameters, 4, ChatColor.RED.getCode());

			final ChatColor leftColor = ChatColor.getByCode(leftColorCode);
			if (leftColor == null) {
				return null;
			}
			final ChatColor rightColor;
			if (rightColorCode == null && parameters[4].parse().equalsIgnoreCase("opaque")) {
				rightColor = null;
			} else {
				rightColor = ChatColor.getByCode(rightColorCode);
				if (rightColor == null) {
					return null;
				}
			}
			final String leftCharacterString = tryParseOptionalAsString(parameters, 5, "|");
			if (leftCharacterString == null || leftCharacterString.length() != 1) {
				return null;
			}
			final char leftChar = leftCharacterString.charAt(0);
			final String rightCharacterString = tryParseOptionalAsString(parameters, 6, leftCharacterString);
			if (rightCharacterString == null || rightCharacterString.length() != 1) {
				return null;
			}
			final char rightChar = rightCharacterString.charAt(0);
			String bar = leftColor + getBar(value, maximum, width, leftChar);
			if (rightColor != null) {
				bar += rightColor + getBar(maximum - value, maximum, width, rightChar);
			}
			return bar;
		} else {
			return null;
		}
	}

}
