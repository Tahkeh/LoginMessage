package com.tahkeh.loginmessage.methods.impl.bukkit;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class ServerTimeMethod extends DefaultNamedMethod<Variables> {

	private final Message message;
	
	public ServerTimeMethod(final Message message) {
		super("srtime", 0, 1);
		this.message = message;
	}

	@Override
	public ParameterType call(Parameter[] parameters, int depth, Variables globalParameters) {
		final SimpleDateFormat dateformat;
		if (parameters.length == 1) {
			dateformat = new SimpleDateFormat(parameters[0].parse().asString());
		} else if (parameters.length == 0) {
			dateformat = this.message.getDateFormat("real", "K:mm a z");
		} else {
			dateformat = null;
		}
		if (dateformat != null) {
			return new StringParameterType(dateformat.format(Calendar.getInstance().getTime()));
		} else {
			return null;
		}
	}

}
