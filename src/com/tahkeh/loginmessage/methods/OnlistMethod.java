package com.tahkeh.loginmessage.methods;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Message;

import de.xzise.MinecraftUtil;
import de.xzise.XLogger;

/**
 * Simple online list method.
 */
public class OnlistMethod implements Method {

	public final static ChatColor NAME_COLOR = ChatColor.WHITE;
	public final static ChatColor COMMA_COLOR = ChatColor.WHITE;

	/**
	 * Tries to convert a string into an integer. If the string is invalid it
	 * returns <code>null</code>.
	 * 
	 * @param string
	 *            The string to be parsed.
	 * @param radix
	 *            The radix of the integer.
	 * @return The value if the string is valid, otherwise <code>null</code>.
	 * @since 1.3
	 */
	// TODO: Move to BPU 1.3
	public static Integer tryAndGetInteger(String string, int radix) {
		try {
			return Integer.parseInt(string, radix);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Parses an integer between 0-16 or 0-F.
	 * @param string the inputed string.
	 * @return the integer or null, if it couldn't parsed.
	 */
	public static Integer parseInt(String string) {
		string = string.trim();
		if (string.length() == 2) {
			return MinecraftUtil.tryAndGetInteger(string);
		} else if (string.length() == 1) {
			return tryAndGetInteger(string, 16);
		} else {
			return null;
		}
	}

	private final XLogger logger;

	public OnlistMethod(XLogger logger) {
		this.logger = logger;
	}

	private ChatColor getColor(String parameter, String name) {
		Integer colorValue = parseInt(parameter);
		if (colorValue == null || colorValue < 0 || colorValue > 15) {
			this.logger.warning("OnList parameter '" + name + "' is not a valid integer ('" + parameter + "'). Valid integers are: 1-16 or 0-F");
			return null;
		} else {
			ChatColor color = ChatColor.getByCode(colorValue);
			if (color == null) {
				this.logger.warning("OnList parameter '" + name + "' is not a valid chat color (" + colorValue + ").");
			}
			return color;
		}
	}

	@Override
	public String call(Player player, String event, String... parameters) {
		ChatColor nameColor = NAME_COLOR;
		ChatColor commaColor = COMMA_COLOR;
		ChatColor endColor = COMMA_COLOR;
		boolean endColorSet = false;
		switch (parameters.length) {
		case 3:
			endColor = getColor(parameters[2], "end color");
			endColorSet = true;
		case 2:
			nameColor = getColor(parameters[0], "name color");
			commaColor = getColor(parameters[1], "comma color");
			break;
		case 0:
			break;
		default:
			return null;
		}
		if (nameColor != null && commaColor != null && endColor != null) {
			if (!endColorSet) {
				endColor = commaColor;
			}
			return this.call(player, event, nameColor, commaColor, endColor);
		} else {
			return null;
		}
	}

	private String call(Player triggerPlayer, String event, ChatColor nameColor, ChatColor commaColor, ChatColor endColor) {
		StringBuilder builder = new StringBuilder();
		List<Player> allPlayers = Arrays.asList(Bukkit.getServer().getOnlinePlayers());
		if (Message.isLeaveEvent(event)) {
			allPlayers.remove(triggerPlayer);
		}
		for (Iterator<Player> playerIt = allPlayers.iterator(); playerIt.hasNext();) {
			Player player = playerIt.next();
			builder.append(nameColor).append(player.getName());
			if (playerIt.hasNext()) {
				builder.append(commaColor).append(", ");
			}
		}
		return builder.append(endColor).toString();
	}

	@Override
	public boolean recursive() {
		return false;
	}
}
