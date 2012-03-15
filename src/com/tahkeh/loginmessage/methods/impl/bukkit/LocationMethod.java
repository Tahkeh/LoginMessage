package com.tahkeh.loginmessage.methods.impl.bukkit;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.DefaultCastedNamedMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public class LocationMethod extends DefaultCastedNamedMethod<BukkitVariables, PlayerVariables> {

	public LocationMethod() {
		super("location", PlayerVariables.class, 0, 1, 2, 3);
	}

	@Override
	public ParameterType innerCall(Parameter[] parameters, PlayerVariables globalParameters) {
		if (globalParameters.offlinePlayer instanceof Player) {
			Location location = ((Player) globalParameters.offlinePlayer).getLocation();
			String format = null;
			boolean[] set = new boolean[3];
			switch (parameters.length) {
			case 0 :
				Arrays.fill(set, true);
				break;
			case 3 :
				Boolean boolZ = parameters[2].parse().asBoolean();
				if (boolZ == null) {
					set = null;
					break;
				} else {
					set[2] = boolZ;
				}
			case 2 :
				Boolean boolY = parameters[1].parse().asBoolean();
				if (boolY == null) {
					set = null;
					break;
				} else if (set != null) {
					set[2] = boolY;
				}
			case 1 :
				Boolean boolX = parameters[0].parse().asBoolean();
				if (boolX == null && parameters.length == 1) {
					format = parameters[0].parse().asString();
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
				return new StringParameterType(String.format(format, location.getBlockX(), location.getBlockY(), location.getBlockZ()));
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
