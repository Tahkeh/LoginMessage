package com.tahkeh.loginmessage.methods.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class ServerTimeMethod extends DefaultNamedMethod {

	private final Message message;
	
	public ServerTimeMethod(final Message message) {
		super(true, "srtime", 0, 1);
		this.message = message;
	}

	@Override
	public String call(OfflinePlayer player, String event, Parameter[] parameters, Variables globalParameters) {
		final SimpleDateFormat dateformat;
		if (parameters.length == 1) {
			dateformat = new SimpleDateFormat(parameters[0].parse());
		} else if (parameters.length == 0) {
			dateformat = this.message.getDateFormat("real", "K:mm a z");
		} else {
			dateformat = null;
		}
		if (dateformat != null) {
			return dateformat.format(Calendar.getInstance().getTime());
		} else {
			return null;
		}
	}

}
