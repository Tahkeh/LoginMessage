package com.tahkeh.loginmessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.matcher.DefaultMatcher;
import com.tahkeh.loginmessage.matcher.entries.Entry;
import com.tahkeh.loginmessage.methods.variables.bukkit.ListVariables;

public class PlayerList {
	private final Main plugin;
	private final boolean online;
	private final boolean formatted;
	private final List<String> groups;
	private final List<String> users;
	private final List<String> permissions;
	private final List<String> worlds;
	private final String format;
	private final String separator;
	private final Player trigger;
	
	public PlayerList(Main plugin, boolean online, boolean formatted, List<String> groups, List<String> users, List<String> permissions, List<String> worlds, String format, String separator, Player trigger) {
		this.plugin = plugin;
		this.online = online;
		this.formatted = formatted;
		this.groups = groups;
		this.users = users;
		this.permissions = permissions;
		this.worlds = worlds;
		this.format = format;
		this.separator = separator;
		this.trigger = trigger;
	}
	
	public String getList() {
		List<OfflinePlayer> players = getPlayers();
		List<String> lines = new ArrayList<String>();
		String s = "";
		String list = "";
		int on = 0;
		int length = players.toArray().length - 1;
		
		if(online && trigger != null) {
			length = length - 1;
		}
		
		for (OfflinePlayer p : players) {
			final ListVariables variables = new ListVariables(p);
			String processedFormat = plugin.msg.processLine(format, variables);
			String processedSeparator = plugin.msg.processLine(separator, variables);
			s = processedSeparator;
			if (!formatted) {
				list = list + (on >= length ? processedFormat : processedFormat + processedSeparator);
			} else {
				lines.add(processedFormat);
			}
			on++;
		}
		
		if (formatted) {
			list = Message.getFormattedString(s, lines.toArray());
		}
		
		return list;
	}

	public List<OfflinePlayer> getPlayers() {
		OfflinePlayer[] playerList = plugin.getServer().getOfflinePlayers();
		List<OfflinePlayer> players = new ArrayList<OfflinePlayer>(playerList.length);
		Set<Entry> entries = getEntries();
		
		for (OfflinePlayer p : playerList) {
			if(DefaultMatcher.match(p, entries)) {
				players.add(p);
			}
		}
		return players;
	}

	public Set<Entry> getEntries() {
		return Message.getEntries(null, plugin, groups, users, permissions, worlds);
	}
}
