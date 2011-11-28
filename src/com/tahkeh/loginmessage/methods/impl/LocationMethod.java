package com.tahkeh.loginmessage.methods.impl;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.DefaultMethod;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.DefaultVariables;

public class LocationMethod extends DefaultMethod {

	public LocationMethod() {
		super(true, 0, 1, 2, 3);
	}

	@Override
	public String call(OfflinePlayer player, String event, Parameter[] parameters, DefaultVariables globalParameters) {
		if (player instanceof Player) {
			Location location = ((Player) player).getLocation();
			String format = null;
			boolean[] set = new boolean[3];
			switch (parameters.length) {
			case 0 :
				Arrays.fill(set, true);
				break;
			case 3 :
				Boolean boolZ = DefaultMethod.parseAsBoolean(parameters[2].parse());
				if (boolZ == null) {
					set = null;
					break;
				} else {
					set[2] = boolZ;
				}
			case 2 :
				Boolean boolY = DefaultMethod.parseAsBoolean(parameters[1].parse());
				if (boolY == null) {
					set = null;
					break;
				} else if (set != null) {
					set[2] = boolY;
				}
			case 1 :
				Boolean boolX = DefaultMethod.parseAsBoolean(parameters[0].parse());
				if (boolX == null && parameters.length == 1) {
					format = parameters[0].parse();
					set = null;
					break;
				} else if (set != null) {
					set[0] = boolX;
				}
			}
			if (format == null) {
				if (set == null) {
					// :( Invalid format
					return null;
				} else {
					for (int i = 0; i < set.length; i++) {
						if (set[i]) {
							if (format == null) {
								format = "";
							} else {
								format += ", ";
							}
							format += "%" + (i + 1) + "$";
						}
					}
				}
			}
			
			if (format != null) {
				return String.format(format, location.getBlockX(), location.getBlockY(), location.getBlockZ());
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
