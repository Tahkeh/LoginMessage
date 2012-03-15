package com.tahkeh.loginmessage.methods.impl;

import java.util.Arrays;

import org.bukkit.ChatColor;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class BarMethod extends DefaultNamedMethod<Variables> {

	public BarMethod() {
		super("bar", 2, 3, 4, 5, 6, 7);
	}

	public static String getBar(int value, int total, int width, char c) {
		return getBar(Math.round((value / (float) total) * width), c);
	}

	public static String getBar(int count, char c) {
		char[] chars = new char[count];
		Arrays.fill(chars, c);
		return new String(chars);
	}

	public static Long tryParseOptionalAsInteger(final Parameter[] parameters, final int index, final int def) {
		return parameters.length > index ? parameters[index].parse().asLong() : def;
	}

	public static String tryParseOptionalAsString(final Parameter[] parameters, final int index, final String def) {
		return parameters.length > index ? parameters[index].parse().asString() : def;
	}

	@Override
	public ParameterType call(Parameter[] parameters, int depth, Variables globalParameters) {
		if (parameters.length >= 2 && parameters.length <= 7) {
			final Long value = parameters[0].parse().asLong();
			if (value == null) {
				return null;
			}
			final Long maximum = parameters[1].parse().asLong();
			if (maximum == null) {
				return null;
			}
			final Long width = tryParseOptionalAsInteger(parameters, 2, 100);
			final Long leftColorCode = tryParseOptionalAsInteger(parameters, 3, ChatColor.GREEN.getCode());
			if (leftColorCode == null) {
				return null;
			}
			final ChatColor leftColor = ChatColor.getByCode(leftColorCode.intValue());
			if (leftColor == null) {
				return null;
			}

			final Long rightColorCode = tryParseOptionalAsInteger(parameters, 4, ChatColor.RED.getCode());
			final ChatColor rightColor;
			if (rightColorCode == null && parameters[4].parse().asString().equalsIgnoreCase("opaque")) {
				rightColor = null;
			} else {
				rightColor = ChatColor.getByCode(rightColorCode.intValue());
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
			String bar = leftColor + getBar(value.intValue(), maximum.intValue(), width.intValue(), leftChar);
			if (rightColor != null) {
				bar += rightColor + getBar(maximum.intValue() - value.intValue(), maximum.intValue(), width.intValue(), rightChar);
			}
			return new StringParameterType(bar);
		} else {
			return null;
		}
	}

}
