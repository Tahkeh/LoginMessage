package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.ChatColor;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class ColorMethod extends DefaultNamedMethod<Variables> {

	public ColorMethod() {
		super("color", 1);
	}

	public static ChatColor getColor(String parameter) {
		if (parameter != null) {
			if (parameter.length() > 1) {
				parameter = parameter.trim();
			}
			if (parameter.length() != 1) {
				return null;
			} else {
				return ChatColor.getByChar(parameter);
			}
		} else {
			return null;
		}
	}

	@Override
	public ParameterType call(Parameter[] parameters, int depth, Variables globalParameters) {
		if (parameters.length == 1) {
			final ChatColor color = getColor(parameters[0].parse().asString());
			if (color != null) {
				return new StringParameterType(color.toString());
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
