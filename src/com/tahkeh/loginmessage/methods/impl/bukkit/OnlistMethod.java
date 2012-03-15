package com.tahkeh.loginmessage.methods.impl.bukkit;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

import de.xzise.MinecraftUtil;

/**
 * Simple online list method.
 */
public class OnlistMethod extends DefaultNamedMethod<BukkitVariables> {

	public final static String PREFIX = "&" + ChatColor.WHITE;
	public final static String SUFFIX = "&" + ChatColor.WHITE;
	public final static String DELIMITER = ", ";

	private final Message message;

	public OnlistMethod(final Message message) {
		super("onlist", 0, 1, 2, 3, 4);
		this.message = message;
	}

	private static String maybeColored(final String string) {
		if (string.matches("[0-9a-fA-F]{1}")) {
			return "&" + string;
		} else {
			return string;
		}
	}
	@Override
	public ParameterType call(Parameter[] parameters, int depth, BukkitVariables globalParameters) {
		String prefix = PREFIX;
		String suffix = SUFFIX;
		String delimiter = DELIMITER;
		boolean useDisplayNames = false;
		switch (parameters.length) {
		case 4:
			delimiter = parameters[3].parse().asString();
		case 3:
			useDisplayNames = parameters[2].parse().asBoolean();
		case 2:
			prefix = maybeColored(parameters[0].parse().asString());
			suffix = maybeColored(parameters[1].parse().asString());
			break;
		case 1:
			return new StringParameterType(this.message.processOnlineList(parameters[0].parse().asString(), globalParameters.leaveEvent && globalParameters instanceof PlayerVariables ? MinecraftUtil.cast(Player.class, ((PlayerVariables) globalParameters).offlinePlayer) : null));
		case 0:
			break;
		default:
			return null;
		}
		return new StringParameterType(this.call(globalParameters, prefix, suffix, useDisplayNames, delimiter));
	}

	private String call(final BukkitVariables variables, final String prefix, final String suffix, final boolean useDisplayNames, final String delimiter) {
		StringBuilder builder = new StringBuilder();
		List<Player> allPlayers = Arrays.asList(Bukkit.getServer().getOnlinePlayers());
		if (variables.leaveEvent && variables instanceof PlayerVariables) {
			allPlayers.remove(((PlayerVariables) variables).offlinePlayer);
		}
		for (Iterator<Player> playerIt = allPlayers.iterator(); playerIt.hasNext();) {
			Player player = playerIt.next();
			builder.append(prefix);
			if (useDisplayNames) {
				builder.append(player.getDisplayName());
			} else {
				builder.append(player.getName());
			}
			if (playerIt.hasNext()) {
				builder.append(suffix).append(delimiter);
			}
		}
		return builder.toString();
	}
}
