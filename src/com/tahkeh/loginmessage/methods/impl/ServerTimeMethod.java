package com.tahkeh.loginmessage.methods.impl;

import java.util.Calendar;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.DefaultVariables;

public class ServerTimeMethod extends OriginalMethod {

	private final Message message;
	
	public ServerTimeMethod(final Message message) {
		this.message = message;
	}

	@Override
	public String call(OfflinePlayer player, String event, DefaultVariables globalParameters) {
		return this.message.getDateFormat().format(Calendar.getInstance().getTime());
	}

}
