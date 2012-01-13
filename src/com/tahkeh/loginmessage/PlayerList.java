package com.tahkeh.loginmessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.entries.DefaultEntry;
import com.tahkeh.loginmessage.entries.Entry;
import com.tahkeh.loginmessage.entries.Group;
import com.tahkeh.loginmessage.entries.Op;
import com.tahkeh.loginmessage.entries.Permission;
import com.tahkeh.loginmessage.entries.Pub;
import com.tahkeh.loginmessage.entries.User;
import com.tahkeh.loginmessage.entries.World;

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
			String processedFormat = plugin.msg.processLine(format, p, "list", null);
			String processedSeparator = plugin.msg.processLine(separator, p, "list", null);
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
		List<OfflinePlayer> players = new ArrayList<OfflinePlayer>();
		Set<Entry> entries = getEntries();
		
		for (OfflinePlayer p : plugin.getServer().getOfflinePlayers()) {
			if(Message.matchEntries(p, entries)) {
				players.add(p);
			}
		}
		return players;
	}
	
	public Set<Entry> getEntries() {
		Set<Entry> entries = new HashSet<Entry>();
		for (String group : groups) {
			boolean positive = DefaultEntry.isPositive(group);
			String unsignedGroup = DefaultEntry.getUnsignedText(group);
			if (unsignedGroup.equalsIgnoreCase("pub")) {
				entries.add(new Pub(null));
			} else if (unsignedGroup.equalsIgnoreCase("op")) {
				entries.add(new Op(positive));
			} else {
				entries.add(new Group(group, Main.getPermissions(), plugin));
			}
		}

		for (String user : users) {
			entries.add(new User(user));
		}

		for (String perm : permissions) {
			entries.add(new Permission(perm, Main.getPermissions(), plugin));
		}
		
		for (String world : worlds) {
			entries.add(new World(world, plugin));
		}
		return entries;
	}
}
