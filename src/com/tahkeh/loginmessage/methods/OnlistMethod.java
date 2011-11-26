package com.tahkeh.loginmessage.methods;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.variables.DefaultVariables;

import de.xzise.XLogger;

/**
 * Simple online list method.
 */
public class OnlistMethod extends DefaultMethod {

	public final static ChatColor NAME_COLOR = ChatColor.WHITE;
	public final static ChatColor COMMA_COLOR = ChatColor.WHITE;

	private final XLogger logger;

	public OnlistMethod(XLogger logger) {
		super(false, 0, 2, 3);
		this.logger = logger;
	}

	private ChatColor getColor(String parameter, String name) {
		Integer colorValue = DefaultMethod.parseAsInteger(parameter);
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
	public String call(OfflinePlayer player, String event, String[] parameters, DefaultVariables globalParameters) {
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

	private String call(OfflinePlayer triggerPlayer, String event, ChatColor nameColor, ChatColor commaColor, ChatColor endColor) {
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
}
