package com.tahkeh.loginmessage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

import com.tahkeh.loginmessage.entries.DefaultEntry;
import com.tahkeh.loginmessage.entries.Entry;
import com.tahkeh.loginmessage.entries.Group;
import com.tahkeh.loginmessage.entries.Op;
import com.tahkeh.loginmessage.entries.Permission;
import com.tahkeh.loginmessage.entries.Pri;
import com.tahkeh.loginmessage.entries.Pub;
import com.tahkeh.loginmessage.entries.User;
import com.tahkeh.loginmessage.entries.causes.Cause;
import com.tahkeh.loginmessage.handlers.AFKHandler;
import com.tahkeh.loginmessage.handlers.DeathHandler;
import com.tahkeh.loginmessage.store.MaterialTable;
import com.tahkeh.loginmessage.store.Store;
import com.tahkeh.loginmessage.timers.Cooldown;
import com.tahkeh.loginmessage.timers.Delay;
import com.tahkeh.loginmessage.timers.Cooldown.CooldownTask;
import com.tahkeh.loginmessage.timers.Interval;

import de.xzise.XLogger;
import de.xzise.wrappers.economy.EconomyHandler;
import de.xzise.wrappers.permissions.BufferPermission;
import de.xzise.wrappers.permissions.PermissionsHandler;

public class Message
{
	public final static char SECTION_SIGN = '\u00A7';
	private final static String[] EMPTY_STRING_ARRAY = new String[0];
	private final static BufferPermission<String> PREFIX_PERMISSION = BufferPermission.create("prefix", (String) null);
	private final static BufferPermission<String> SUFFIX_PERMISSION = BufferPermission.create("suffix", (String) null);

	private final Main plugin;
	private final Configuration config;
	private final Configuration message;
	private final Configuration list;
	private final XLogger logger;
	private final Store store;
	private final MaterialTable table;

	String separator = "%&%&";
	Set<String> kicked = new HashSet<String>();
	List<String> running = new ArrayList<String>();
	
	private final Cooldown cooldown;
	
	public Message(Main plugin, Configuration config, Configuration message, Configuration list, XLogger logger, Store store, MaterialTable table) {
		this.plugin = plugin;
		this.config = config;
		this.message = message;
		this.list = list;
		this.logger = logger;
		this.cooldown = new Cooldown();
		this.store = store;
		this.table = table;
		}
	
	public void load(String event) {
		if(config.getBoolean("autoload", true) || event.equals("load")) {
			config.load();
			message.load();
			list.load();
		}
		separator = config.getString("separator", "%&%&");
		store.load(event);
		scheduleIntervals(event);
	}
	
	public void unload() {
		plugin.getServer().getScheduler().cancelTasks(plugin);
		running.clear();
	}
	
	public void scheduleIntervals(String event) {
		List<String> keys = message.getKeys("messages.interval");
		if(keys != null && !event.equals("interval")) {
			for(String key : keys) {
				if(!running.contains(key)) {
					int interval = message.getInt("messages.interval." + key + ".interval", 300);
					plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Interval(this, key), interval * 20, interval * 20);
					running.add(key);
				}
			}
		}
	}
	
	public String getTimeDifference(long start) {
		List<String> lines = new ArrayList<String>();
		Date end = Calendar.getInstance().getTime();
		long difference = (end.getTime() - start) / 1000;
		long date[] = new long[] {0, 0, 0, 0};
		
		date[3] = (difference >= 60 ? difference % 60 : difference);
		date[2] = (difference = (difference / 60)) >= 60 ? difference % 60 : difference;
		date[1] = (difference = (difference / 60)) >= 24 ? difference % 24 : difference;
		date[0] = (difference = (difference / 24));
		
		long days = date[0];
		long hours = date[1];
		long minutes = date[2];
		long seconds = date[3];
		
		if (days > 0) {
			lines.add(String.format("%d day%s", days, days != 1 ? "s" : ""));
		}
		if (hours > 0) {
			lines.add(String.format("%d hour%s", hours, hours != 1 ? "s" : ""));
		}
		if (minutes > 0) {
			lines.add(String.format("%d minute%s", minutes, minutes != 1 ? "s" : ""));
		}
		if (seconds > 0) {
			lines.add(String.format("%d second%s", seconds, seconds != 1 ? "s" : ""));
		} else if (difference == 0) {
			lines.add("a moment");
		}
		
		return getFormattedString("", lines.toArray());
	}
	
	public static String processColors(String string) {
		return string.replaceAll("(&([a-z0-9]))", SECTION_SIGN + "$2");
	}

	public static Player getPlayer(OfflinePlayer player) {
		return player instanceof Player ? (Player) player : player.isOnline() ? Bukkit.getPlayerExact(player.getName()) : null;
	}

	/**
	 * Transform an array of objects into a readable string.
	 * @param string
	 * 		the array to transform
	 * @return a readable string
	 */
	public static String getFormattedString(String s, Object... string) {
		StringBuilder sb = new StringBuilder();
		int length = string.length;
		int count = 0;
		String separator = s.equals("") ? ", " : s;
		
		for (Object item : string) {
			count = count + 1;
			if (length == 1) {
				sb.append(item);
			} else if (length == 2) {
				if (count == 1) {
					sb.append(item + " and ");
				} else {
					sb.append(item);
				}
			} else {
				if (count == length - 1) {
					sb.append(item + separator + "and ");
				} else if (count == length) {
					sb.append(item);
				} else {
					sb.append(item + separator);
				}
			}
		}
		
		return sb.toString();	
	}
	
	/**
	 * Check the .dat files in the default world folder. If the given player is 
	 * found, return true.
	 * @param p
	 * 			the name of the player to look for
	 * @return whether or not player 'p' has joined the server before
	 */
	public boolean existingPlayer(String p) {
		String pdir = getDefaultWorld().getName() + File.separator + "players";
		File playerfile = new File(pdir);
		String[] playerfiles = playerfile.list();
		for(String player : playerfiles) {
			if(player.contains(p)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get the first loaded, or default world.
	 * @return the default world
	 */
	public World getDefaultWorld() {
		  return plugin.getServer().getWorlds().get(0);
		}
	
	public String getLocation(String type, String p, String event) {
		return store.getLocation(type, p);
	}
	
	public String getTime(Long rawtime, boolean caps) {
		String day = config.getString("day");
		String dusk = config.getString("sunset");
		String dawn = config.getString("sunrise");
		String night = config.getString("night");

		int modTime = (int) (rawtime % 24000);

		String name = "";
		if (modTime == 24000 || modTime <= 11999) {
			name = day;
		} else if (modTime == 12000 || modTime <= 12999) {
			name = dusk;
		} else if (modTime == 13000 || modTime <= 22999) {
			name = night;
		} else if (modTime == 23000 || modTime <= 23999) {
			name = dawn;
		}
		return caps ? toCapitalCase(name) : name.toLowerCase();
	}
	
	public String getGameMode(GameMode mode, boolean caps) {
		String name = "";
		
		if(mode == GameMode.CREATIVE) {
			name = "Creative";
		} else if (mode == GameMode.SURVIVAL) {
			name = "Survival";
		}
		return caps ? name : name.toLowerCase();
	}
	
	private static <T> T getFirst(T[] array) {
		return getFirst(array, null);
	}

	private static <T> T getFirst(T[] array, T def) {
		if (array != null && array.length > 0) {
			return array[0];
		} else {
			return def;
		}
	}

	private static String getPrefix(String group, String world) {
		return Main.getPermissions().getString(world, group, PREFIX_PERMISSION);
	}

	private static String getSuffix(String group, String world) {
		return Main.getPermissions().getString(world, group, SUFFIX_PERMISSION);
	}

	public boolean isLeaveEvent(String event) // For %ol and %size
	{
		return event.equals("kick") || event.equals("quit");
	}

	public String booleanToName(boolean bool, boolean caps) {
		String t = config.getString("istrue", "&2Yes");
		String f = config.getString("isfalse", "&4No");
		String name = "";

		if (bool) {
			name = t;
		} else {
			name = f;
		}
		
		name = processColors(name);

		return caps ? name : name.toLowerCase();
	}
	
	public String getStatus(OfflinePlayer p, boolean caps) {
		AFKHandler afkhandler = new AFKHandler(plugin);
		String status = "";
		String online = config.getString("status.online", "&2Online");
		String offline = config.getString("status.offline", "&7Offline");
		String afk = config.getString("status.afk", "&6AFK");
		
		if (p.isOnline()) {
			status = caps ? toCapitalCase(online) : online.toLowerCase();
			if (afkhandler.isActive()) {
				if (afkhandler.isAFK(plugin.getServer().getPlayerExact(p.getName()))) {
					status = afk;
				}
			}
		} else {
			status = caps ? toCapitalCase(offline) : offline.toLowerCase();
		}
		
		status = processColors(status);
		
		return status;
	}
	
	public String textProcess(String str) {
		boolean vowel = false;
		if (str.contains("%an%")) {
			String code = str.substring(str.indexOf("%an%"), str.indexOf("%an%") + 4);
			String letter = str.substring(str.indexOf("%an%") + 5, str.indexOf("%an%") + 6);
			if (letter.equalsIgnoreCase("a") || letter.equalsIgnoreCase("e") || letter.equalsIgnoreCase("i") || letter.equalsIgnoreCase("o") || letter.equalsIgnoreCase("u")) {
				vowel = true;
			}
			if (vowel) {
				str = str.replace(code, "an");
			} else {
				str = str.replace(code, "a");
			}
		}
		if (str.contains("%ifeq(")) {
			int start = str.indexOf("%ifeq(") + 6;
			int end = str.indexOf(")", start);
			String trim = str.substring(start, end);
			String regex = "%ifeq(" + trim + ")";
			String replacement = "";
			
			String boolean1 = trim.substring(0, trim.indexOf(","));
			String trim1 = trim.substring(trim.indexOf(",") + 2);
			
			String boolean2 = trim1.substring(0, trim1.indexOf(","));
			String trim2 = trim1.substring(trim1.indexOf(",") + 2);
			
			String equal = trim2.substring(0, trim2.indexOf(","));
			String unequal = trim2.substring(trim2.indexOf(",") + 2);
			
			if (boolean1.equals(boolean2)) {
				replacement = equal;
			} else {
				replacement = unequal;
			}
			
			str = str.replace(regex, replacement);
		}
		
		return str;
	}
	
	public String processOnlineList(String str, Player p, String event) {
		PermissionsHandler handler = Main.getPermissions();
		if (str.contains("%ol" + separator)) {
			String list = "";
			int on = 0;
			Player[] players = plugin.getServer().getOnlinePlayers();
			int length = players.length - 1;
			List<Player> online = new ArrayList<Player>();
			String s = str;
			String code = "%ol" + separator;
			while(s.indexOf(code) >= 0) {
				s = s.substring(s.indexOf(code) + 1);
			}
			for (Player all : players) {
				while(!online.contains(all)) {
					online.add(all);
				}
				if(isLeaveEvent(event)) {
					online.remove(p);
					length = length - 1;
				}
			}
			if (s.substring(code.length() - 1, s.indexOf(":")).length() == 1 || s.substring(code.length() - 1, s.indexOf(":")).length() == 0) {
				String a = s.substring(code.length() - 1, s.indexOf(":"));
		        s = s.substring(s.indexOf(":") + 1);
		        String b = s.substring(0, s.indexOf(":"));
		        s = s.substring(s.indexOf(":") + 1);
		        String c = s.substring(0, 2);
		        for(Player all : online) {
		        	String prefix = b;
		        	String suffix = c;
		        	String name = a.equalsIgnoreCase("d") ? all.getDisplayName() : all.getName();
		        	if(handler.isActive()) {
		        		String world = all.getWorld().getName();
		        		String group = getFirst(handler.getGroup(world, all.getName()));
		        		prefix = prefix.replaceAll("pr", getPrefix(group, world));
		        		prefix = prefix.replaceAll("sf", getSuffix(group, world));
		        		suffix = suffix.replaceAll("pr", getPrefix(group, world));
		        		suffix = suffix.replaceAll("sf", getSuffix(group, world));
		        	}
		        	prefix = processColors(prefix);
		        	suffix = processColors(suffix);
		        	list = list + (on >= length ? prefix + name + suffix : prefix + name + suffix + ", ");
		        	on++;
		        }
		        String ol = code + a + ":" + b + ":" + c;
		        str = str.replaceAll(ol, list);
			}
		}
		if (str.contains("%ol_")) {
			List<String> pub = new ArrayList<String>();
			pub.add("pub");
			if (list.getKeys("lists") != null) {
				for (String key : list.getKeys("lists")) {
					if (str.contains("%ol_" + key)) {
						String path = "lists." + key;
						boolean online = list.getBoolean(path + ".online", true);
						boolean formatted = list.getBoolean(path + ".formatted", false);
						List<String> groups = list.getStringList(path + ".players.groups", pub);
						List<String> users = list.getStringList(path + ".players.users", null);
						List<String> permissions = list.getStringList(path + ".players.permissions", null);
						List<String> worlds = list.getStringList(path + ".players.worlds", null);
						String format = list.getString(path + ".format", "%nm");
						String separator = list.getString(path + ".separator", ", ");
						Player trigger = isLeaveEvent(event) ? p : null;
						PlayerList playerList = new PlayerList(plugin, online, formatted, groups, users, permissions, worlds, format, separator, trigger);
						str = str.replaceAll("%ol_" + key, playerList.getList());
					}
				}
			}
		}
		if (str.contains("%ol")) {
			String list = "";
			int on = 0;
			Player[] players = plugin.getServer().getOnlinePlayers();
			int length = players.length - 1;
			List<Player> online = new ArrayList<Player>();
			for(Player all : players) {
				while(!online.contains(all)) {
					online.add(all);
				}
				if(isLeaveEvent(event)) {
					online.remove(p);
					length = length - 1;
				}
			}
			for(Player all : online) {
				list = list + (on >= length ? all.getName() : all.getName() + ", ");
				on++;
			}
			str = str.replaceAll("%ol", list);
		}
		
		return str;
	}

	public String processLine(String str, OfflinePlayer p, String event, Map<String, String> args) {
		Player trigger = null;
		int size = plugin.getServer().getOnlinePlayers().length;
		SimpleDateFormat sdf = new SimpleDateFormat(config.getString("format", "K:mm a z"));
		String[] onlineCodes = {
				"%world", "%rtime", "%time", "%Time", "%Mode", "%mode",
				"%asleep", "%Asleep", "%x", "%y", "%z", "%level",
				"%curxp", "%totalxp", "%food", "%exhaust", "%sat", "%ip"
				};
		String[] playerCodes = {
				"%laston", "%nm", "%online", "%Online", "%status", "%Status", "%banned", "%Banned",
				"%white", "%White", "%op", "%Op", "%city", "%ccode", "%zip", "%rcode", "%rname"
		};
		
		if (isLeaveEvent(event)) {
			size = size - 1;
		}
		
		if(p != null) {
			str = str.replaceAll("%laston", getTimeDifference(store.getLastLogin(p.getName())));		
			str = str.replaceAll("%nm", p.getName());
			str = str.replaceAll("%status", getStatus(p, false));
			str = str.replaceAll("%Status", getStatus(p, true));
			str = str.replaceAll("%online", booleanToName(p.isOnline(), false));
			str = str.replaceAll("%Online", booleanToName(p.isOnline(), true));
			str = str.replaceAll("%banned", booleanToName(p.isBanned(), false));
			str = str.replaceAll("%Banned", booleanToName(p.isBanned(), true));
			str = str.replaceAll("%white", booleanToName(p.isWhitelisted(), false));
			str = str.replaceAll("%White", booleanToName(p.isWhitelisted(), true));
			str = str.replaceAll("%op", booleanToName(p.isOp(), false));
			str = str.replaceAll("%Op", booleanToName(p.isOp(), true));

			if (str.contains("%city")) {
				str = str.replaceAll("%city", getLocation("city", p.getName(), event));
			}
			if (str.contains("%ccode")) {
				str = str.replaceAll("%ccode", getLocation("ccode", p.getName(), event));
			}
			if (str.contains("%cname")) {
				str = str.replaceAll("%cname", getLocation("cname", p.getName(), event));
			}
			if (str.contains("%zip")) {
				str = str.replaceAll("%zip", getLocation("zip", p.getName(), event));
			}
			if (str.contains("%rcode")) {
				str = str.replaceAll("%rcode", getLocation("rcode", p.getName(), event));
			}
			if (str.contains("%rname")) {
				str = str.replaceAll("%rname", getLocation("rname", p.getName(), event));
			}
			
			if (p.isOnline()) {
				trigger = plugin.getServer().getPlayerExact(p.getName());
				str = onlineProcess(str, plugin.getServer().getPlayerExact(p.getName()), event, args);
			} else if (!event.equals("list")) {
				trigger = plugin.getServer().getPlayerExact(args.get("trigger"));
				str = str.replaceAll("%dpnm", p.getName());
				for (String code : onlineCodes) {
					str = str.replaceAll(code, "?"); // Return ? if an online code is used for an offline player (shouldn't happen anyways)
				}
			}
		} else {
			for(String code : playerCodes) {
				str = str.replaceAll(code, "?");
			}
		}
		if (!event.equals("list")) {
			str = processOnlineList(str, trigger, event);
		}
		str = str.replaceAll("%size", Integer.toString(size));
		str = str.replaceAll("%max", Integer.toString(plugin.getServer().getMaxPlayers()));
		str = str.replaceAll("%srtime", sdf.format(Calendar.getInstance().getTime()));
		
		str = processColors(str);
		str = str.replaceAll("%sp", "");

		return textProcess(str);
	}

	public String onlineProcess(String str, Player p, String event, Map<String, String> args) {
		EconomyHandler economy = Main.getEconomy();
		PermissionsHandler permissions = Main.getPermissions();
		String ip = !isLocal(p) ? p.getAddress().getAddress().getHostAddress() : Main.getExternalIp().getHostAddress();
		Long rawtime = p.getWorld().getTime();

		str = eventProcess(str, p, event, args);

		str = str.replaceAll("%dpnm", p.getDisplayName());
		str = str.replaceAll("%world", p.getWorld().getName());
		str = str.replaceAll("%rtime", rawtime.toString());
		str = str.replaceAll("%time", getTime(rawtime, false));
		str = str.replaceAll("%Time", getTime(rawtime, true));
		str = str.replaceAll("%mode", getGameMode(p.getGameMode(), false));
		str = str.replaceAll("%Mode", getGameMode(p.getGameMode(), true));
		str = str.replaceAll("%asleep", booleanToName(p.isSleeping(), false));
		str = str.replaceAll("%Asleep", booleanToName(p.isSleeping(), true));
		str = str.replaceAll("%x", Integer.toString(p.getLocation().getBlockX()));
		str = str.replaceAll("%y", Integer.toString(p.getLocation().getBlockY()));
		str = str.replaceAll("%z", Integer.toString(p.getLocation().getBlockZ()));
		str = str.replaceAll("%level", Integer.toString(p.getLevel()));
		str = str.replaceAll("%curxp", Integer.toString(p.getExperience()));
		str = str.replaceAll("%totalxp", Integer.toString(p.getTotalExperience()));
		str = str.replaceAll("%food", Integer.toString(p.getFoodLevel()));
		str = str.replaceAll("%exhaust", Float.toString(p.getExhaustion()));
		str = str.replaceAll("%sat", Float.toString(p.getSaturation()));
		str = str.replaceAll("%ip", ip);
		
		if (economy.isActive()) {
			str = str.replaceAll("%bal", Double.toString(economy.getBalance(p.getName())));
		}
		
		if (permissions.isActive()) {
			String world = p.getWorld().getName();
			String groupname = getFirst(permissions.getGroup(world, p.getName()));
			str = str.replaceAll("%group", groupname);
			if (getPrefix(groupname, world) != null) {
				str = str.replaceAll("%prefix", getPrefix(groupname, world));
			}
			if (getSuffix(groupname, world) != null) {
				str = str.replaceAll("%suffix", getSuffix(groupname, world));
			}
		}

		return str;
	}

	public String eventProcess(String str, Player p, String event, Map<String, String> args) {
		if (event.equals("kick")) {
			str = str.replaceAll("%reason", args.get("kickreason"));
		}
		if (event.equals("death")) {
			str = str.replaceAll("%ditem", args.get("item"));
			str = str.replaceAll("%dentity", args.get("entity"));
		}

		return str;
	}

	public boolean isLocal(Player p) {
		return store.isLocal(p);
	}

	public Set<Entry> getEntries(Player trigger, String key, String event, String type) // For receivers/triggers
	{
		Set<Entry> entries = new HashSet<Entry>();
		final String keypath = "messages." + event + "." + key + "." + type;
		final String userpath = keypath + ".users";
		final String grouppath = keypath + ".groups";
		final String permspath = keypath + ".permissions";
		final String worldpath = keypath + ".worlds";
		for (String group : message.getStringList(grouppath, null)) {
			boolean positive = DefaultEntry.isPositive(group);
			String unsignedGroup = DefaultEntry.getUnsignedText(group);
			if (unsignedGroup.equalsIgnoreCase("pub")) {
				entries.add(new Pub(positive ? null : trigger));
			} else if (unsignedGroup.equalsIgnoreCase("op")) {
				entries.add(new Op(positive));
			} else if (unsignedGroup.equalsIgnoreCase("pri")) {
				entries.add(new Pri(positive, trigger));
			} else {
				entries.add(new Group(group, Main.getPermissions(), plugin));
			}
		}

		for (String user : message.getStringList(userpath, null)) {
			entries.add(new User(user));
		}

		for (String perm : message.getStringList(permspath, null)) {
			entries.add(new Permission(perm, Main.getPermissions(), plugin));
		}
		
		for (String world : message.getStringList(worldpath, null)) {
			entries.add(new com.tahkeh.loginmessage.entries.World(world, plugin));
		}
		return entries;
	}

	/**
	 * Prints all messages which the player is triggering.
	 * 
	 * @param trigger
	 *            the player who act as trigger.
	 * @param event
	 *            the event type (e.g. login, kick,...).
	 * @param cmdkey
	 *            the name of the message (for the commands). Will be ignored if
	 *            the event isn't <code>command</code>.
	 */
	public void preProcessMessage(OfflinePlayer trigger, String event, Map<String, String> args) {		
		String[] messages;
		boolean cont = true;
		if (event.equals("command")) {
			messages = new String[] { args.get("cmd") };
		} else {
			List<String> keyList = message.getKeys("messages." + event);
			if (keyList == null) {
				messages = EMPTY_STRING_ARRAY;
			} else {
				messages = keyList.toArray(EMPTY_STRING_ARRAY);
			}
		}
		for (String key : messages) {
			if (event.equals("death") && !key.equals(args.get("key"))) {
				cont = false;
			} else {
				cont = true;
			}
			if (cont) {
				Set<Entry> triggers = null;
				String name = args != null && args.containsKey("trigger") ? args.get("trigger") : trigger.getName();
				Player trueTrigger = plugin.getServer().getPlayerExact(name);
				triggers = getEntries(trueTrigger, key, event, "triggers");
				if (matchEntries(trueTrigger, triggers)) {
					finishMessage(trigger, event, key, args);
				}
			}
		}
	}

	public static boolean matchEntries(OfflinePlayer player, Collection<Entry> entries) {
		boolean match = false;
		for (Entry entry : entries) {
			if (entry.match(player)) {
				if (!entry.isPositive()) {
					return false;
				} else {
					match = true;
				}
			}
		}
		return match;
	}

	/**
	 * Returns the list of not empty message lines.
	 * 
	 * @param event
	 *            the event name of the message.
	 * @param name
	 *            the name of the message.
	 * @return the list of not empty message lines.
	 */
	private String[] getLines(String event, String name) {
		List<ConfigurationNode> messages = this.message.getNodeList("messages." + event + "." + name + ".messages", null);
		String[] lines = EMPTY_STRING_ARRAY;
		if (messages != null && messages.size() > 0) {
			// See: MinecraftUtil.getRandomFromChances
			// Read chances
			double totalchance = 0;
			double defChance = 1.0 / messages.size();
			for (ConfigurationNode messageNode : messages) {
				totalchance += messageNode.getDouble("chance", defChance);
			}

			double value = Math.random() * totalchance;
			for (ConfigurationNode messageNode : messages) {
				value -= messageNode.getDouble("chance", defChance);
				if (value < 0) {
					lines = getStringList(messageNode, "message", EMPTY_STRING_ARRAY);
					break;
				}
			}
		} else {
			lines = getStringList(message, "messages." + event + "." + name + ".message", EMPTY_STRING_ARRAY);
		}
		List<String> cleanedLines = new ArrayList<String>(lines.length);
		for (int i = 0; i < lines.length; i++) {
			if (lines[i] != null && !lines[i].isEmpty()) {
				cleanedLines.add(lines[i]);
			}
		}
		return cleanedLines.toArray(new String[cleanedLines.size()]);
	}

	/**
	 * Returns a string list from a yml configuration node. If the node points
	 * to a string list it returns the string list and if it is a string, a
	 * string splited by new lines.
	 * 
	 * @param node
	 *            yml node.
	 * @param path
	 *            path to the string.
	 * @param def
	 *            default value.
	 * @return a string list from a yml configuration node.
	 */
	public static String[] getStringList(ConfigurationNode node, String path, String[] def) {
		Object property = node.getProperty(path);
		if (property instanceof List) {
			@SuppressWarnings("unchecked")
			List<Object> rawList = (List<Object>) property;
			List<String> result = new ArrayList<String>(rawList.size());
			for (Object object : rawList) {
				if (object != null) {
					result.add(object.toString());
				}
			}
			return result.toArray(new String[result.size()]);
		} else if (property != null) {
			return property.toString().split("\\n");
		} else {
			return def;
		}
	}
	
	public static String toCapitalCase(String str) {
		//TODO: Search for first instance of a letter in str instead of assuming index 1 is a letter.
		String capital = str.substring(0, 1).toUpperCase();
		String trim = str.substring(1).toLowerCase();
		return capital + trim;
	}

	public void finishMessage(OfflinePlayer p, String event, String key, Map<String, String> args) // Final touches - delay and cooldown
	{
		String[] lines = this.getLines(event, key);
		if (lines.length == 0) {
			this.logger.info("Empty message named '" + key + "' (Event: '" + event + "') found.");
		} else {
			final String keypath = "messages." + event + "." + key;
			int cd = message.getInt(keypath + ".cooldown", 0) * 1000;
			int dl = message.getInt(keypath + ".delay", 0);

			Player[] players = this.plugin.getServer().getOnlinePlayers();
			List<Player> cooledDown = new ArrayList<Player>(players.length);
			CooldownTask task = null;
			Player truetrigger = null;
			if (event.equals("command") && args.containsKey("trigger")) {
				truetrigger = plugin.getServer().getPlayerExact(args.get("trigger"));
			} else if (p != null) {
				truetrigger = plugin.getServer().getPlayerExact(p.getName());
			}
			Set<Entry> receivers = getEntries(truetrigger, key, event, "receivers");

			// Since the only number lower than 1000 that is possible for the
			// cooldown is 0, we make sure it isn't for the same reason as the delay.
			if (cd > 0) {
				List<String> cdstrs = new ArrayList<String>(players.length);
				for (Player player : players) {
					if (this.cooldown.isCooledDown(player, key, event) && matchEntries(player, receivers)) {
						cooledDown.add(player);
						cdstrs.add(Cooldown.createKey(player, key, event));
					}
				}
				task = this.cooldown.createTask(cdstrs, this.cooldown, cd);
			} else {
				for (Player player : players) {
					if (matchEntries(player, receivers)) {
						cooledDown.add(player);
					}
				}
				task = null;
			}

			if (cooledDown.size() > 0) {
				// Check if the delay isn't greater than or equal to 3.
				// Anything below 3 milliseconds makes your computer sad from my
				// experience.
				if (dl >= 3) {
					new Timer().schedule(new Delay(this, lines, p, event, cooledDown, task, args), dl);
				} else {
					sendMessage(p, cooledDown, lines, event, task, args);
				}
			}
		}
	}

	/**
	 * Sends the message to all receivers.
	 * 
	 * @param trigger
	 *            the player who triggered the message.
	 * @param possibleReceivers
	 *            the players who should receive the message if they are
	 *            receivers.
	 * @param key
	 *            the name of the message.
	 * @param event
	 *            the event type of the message (e.g. login).
	 */
	public void sendMessage(OfflinePlayer trigger, Collection<Player> possibleReceivers, String[] lines, String event, CooldownTask task, Map<String, String> args)
	{
		for (Player receiver : possibleReceivers) {
			for (String str : lines) {
				if (!processLine(str, trigger, event, args).trim().equals("")) {
					// Don't send an empty line
					receiver.sendMessage(processLine(str, trigger, event, args));
				}
			}
		}
		if (task != null) {
			task.trigger();
		}
	}

	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		String e = existingPlayer(p.getName()) ? "login" : "firstlogin";
		load(e);
		preProcessMessage(p, e, null);

		if (config.getBoolean("clearjoinmsg", true)) {
			event.setJoinMessage(null);
		}
	}

	public void onPlayerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (!this.kicked.remove(p.getName())) {
			load("quit");
			preProcessMessage(p, "quit", null);

			if (config.getBoolean("clearquitmsg", true)) {
				event.setQuitMessage(null);
			}
		}
	}

	public void onPlayerKick(PlayerKickEvent event) {
		load("kick");
		Player p = event.getPlayer();
		this.kicked.add(p.getName());
		Map<String, String> args = new HashMap<String, String>();

		args.put("kickreason", event.getReason());
		preProcessMessage(p, "kick", args);

		if (config.getBoolean("clearkickmsg", true)) {
			event.setLeaveMessage(null);
		}
	}

	public void onIntervalCompletion(String key) {
		load("interval");
		finishMessage(null, "interval", key, null);
	}
	
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		load("command");
		Player sender = event.getPlayer();
		String[] cmdargs = event.getMessage().substring(1).split(" ");
		Map<String, String> args = new HashMap<String, String>();
		List<String> commands = message.getKeys("messages.command");

		args.put("cmd", cmdargs[0]);
		if (commands != null) {
			for (String key : commands) {
				List<String> msgargs = message.getStringList("messages.command." + key + ".args", null);
				if (key.equalsIgnoreCase(cmdargs[0])) {
					event.setCancelled(true);
					if (cmdargs.length == 1) {
						preProcessMessage(sender, "command", args);
					} else {
						OfflinePlayer target = plugin.getServer().getOfflinePlayer(cmdargs[1]);
						if (msgargs.contains("player")) {
							if (existingPlayer(target.getName())) {
								args.put("trigger", sender.getName());
								preProcessMessage(target, "command", args);
							} else {
								String noplayerfound = config.getString("noplayerfound", "&cPlayer \"%nm\" does not exist!");
								noplayerfound = noplayerfound.replaceAll("%nm", cmdargs[1]);
								noplayerfound = processColors(noplayerfound);
								sender.sendMessage(noplayerfound);
							}
						} else {
							preProcessMessage(sender, "command", args);
						}
					}
				}
			}
		}
	}
	
	public void onPlayerDeath(PlayerDeathEvent event) {
		load("death");
		Map<String, String> args = new HashMap<String, String>();
		Map<String, List<String>> keyCauses = new HashMap<String, List<String>>();
		Player p = (Player) event.getEntity();
		DeathHandler handler = new DeathHandler(p, table);
		List<String> keys = message.getKeys("messages.death");
		
		if (keys != null) {
			for (String key : keys) {
				List<String> triggerCauses = message.getStringList("messages.death." + key + ".causes", null);
				Set<Cause> possibleCauses = handler.getCauses();
				if (triggerCauses != null) {
					keyCauses.put(key, triggerCauses);
				}
				for (String triggerCause : keyCauses.get(key)) {
					if (DeathHandler.matchCauses(toCapitalCase(triggerCause), possibleCauses)) {
						args.put("key", key);
						args.put("entity", handler.getKiller());
						args.put("item", handler.isKillerPlayer() ? handler.getItem(plugin.getServer().getPlayerExact(handler.getKiller()).getItemInHand()) : "?");
						preProcessMessage(p, "death", args);
						break;
					}
				}
			}
		}
		
		if (config.getBoolean("cleardeathmsg", true)) {
			event.setDeathMessage(null);
		}
	}
}