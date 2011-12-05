package com.tahkeh.loginmessage.methods.impl;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.DefaultMethod;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class ColorMethod extends DefaultMethod {

	public ColorMethod() {
		super(true, 1);
	}

	@Override
	public String call(OfflinePlayer player, String event, Parameter[] parameters, Variables globalParameters) {
		if (parameters.length == 1) {
			Integer i = DefaultMethod.parseAsInteger(parameters[0].parse());
			if (i != null) {
				return Message.SECTION_SIGN + Integer.toHexString(i);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
