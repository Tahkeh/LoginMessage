package com.tahkeh.loginmessage.methods.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

public class DecimalTimeMethod extends DefaultNamedMethod {

	private final Message message;

	public DecimalTimeMethod(final Message message) {
		super(true, "dtime", 0, 1);
		this.message = message;
	}

	@Override
	public String call(OfflinePlayer player, String event, Parameter[] parameters, Variables globalParameters) {
		if (player instanceof Player) {
			final SimpleDateFormat dateformat;
			if (parameters.length == 1) {
				dateformat = new SimpleDateFormat(parameters[0].parse());
			} else if (parameters.length == 0) {
				dateformat = this.message.getDateFormat("virtual", "kk:mm");
			} else {
				dateformat = null;
			}
			if (dateformat != null) {
				// Worldtime has an offset of 6 hours (00000 means 06:00)!
				final int rawtime = (int) ((((Player) player).getWorld().getTime() + 6000) % 24000);
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR_OF_DAY, rawtime / 1000);
				final double minute = (rawtime % 1000) * 0.06;
				calendar.set(Calendar.MINUTE, (int) Math.floor(minute));
				calendar.set(Calendar.SECOND, (int) Math.floor((minute * 60) % 60));
				calendar.set(Calendar.MILLISECOND, (int) Math.floor((minute * 60000) % 1000));
				return dateformat.format(calendar.getTime());
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
