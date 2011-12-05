package com.tahkeh.loginmessage.methods;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.MinecraftUtil;

/**
 * Simple online list method.
 */
public class OnlistMethod extends DefaultMethod {

	public final static String PREFIX = "&" + ChatColor.WHITE;
	public final static String SUFFIX = "&" + ChatColor.WHITE;
	public final static String DELIMITER = ", ";

	private final Message message;

	public OnlistMethod(final Message message) {
		super(false, 0, 1, 2, 3, 4);
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
	public String call(OfflinePlayer player, String event, Parameter[] parameters, Variables globalParameters) {
		String prefix = PREFIX;
		String suffix = SUFFIX;
		String delimiter = DELIMITER;
		boolean useDisplayNames = false;
		switch (parameters.length) {
		case 4:
			delimiter = parameters[3].parse();
		case 3:
			useDisplayNames = DefaultMethod.parseAsBoolean(parameters[2].parse());
		case 2:
			prefix = maybeColored(parameters[0].parse());
			suffix = maybeColored(parameters[1].parse());
			break;
		case 1:
			return this.message.processOnlineList(parameters[0].parse(), Message.isLeaveEvent(event) ? MinecraftUtil.cast(Player.class, player) : null);
		case 0:
			break;
		default:
			return null;
		}
		return this.call(player, event, prefix, suffix, useDisplayNames, delimiter);
	}

	private String call(final OfflinePlayer triggerPlayer, final String event, final String prefix, final String suffix, final boolean useDisplayNames, final String delimiter) {
		StringBuilder builder = new StringBuilder();
		List<Player> allPlayers = Arrays.asList(Bukkit.getServer().getOnlinePlayers());
		if (Message.isLeaveEvent(event)) {
			allPlayers.remove(triggerPlayer);
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
