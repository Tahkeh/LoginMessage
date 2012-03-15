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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
import com.tahkeh.loginmessage.handlers.PlayerDataHandler;
import com.tahkeh.loginmessage.store.MaterialTable;
import com.tahkeh.loginmessage.store.PropertiesFile;
import com.tahkeh.loginmessage.timers.Cooldown;
import com.tahkeh.loginmessage.timers.Delay;
import com.tahkeh.loginmessage.timers.Cooldown.CooldownTask;
import com.tahkeh.loginmessage.timers.Interval;

import de.xzise.MinecraftUtil;
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
	private final FileConfigurationPair<YamlConfiguration> config;
	private final FileConfigurationPair<YamlConfiguration> message;
	private final FileConfigurationPair<YamlConfiguration> list;
	private final XLogger logger;
	private final MaterialTable table;
	private final PlayerDataHandler data;
	String separator = "%&%&";
	Set<String> kicked = new HashSet<String>();
	List<String> running = new ArrayList<String>();
	Map<String, Integer> cycle = new HashMap<String, Integer>();
	Map<String, String> geoipFail = new HashMap<String, String>();
	
	private final Cooldown cooldown;

	public Message(Main plugin, FileConfigurationPair<YamlConfiguration> config, FileConfigurationPair<YamlConfiguration> message, FileConfigurationPair<YamlConfiguration> list, XLogger logger, MaterialTable table) {
		this.plugin = plugin;
		this.config = config.load();
		this.message = message.load();
		this.list = list.load();
		this.logger = logger;
		this.cooldown = new Cooldown();
		this.table = table;
		addGeoipFail();
		File geoip = new File(plugin.getDataFolder(), "GeoLiteCity.dat");
		PropertiesFile store = new PropertiesFile(new File(plugin.getDataFolder(), "store.txt"), logger);
		this.data = new PlayerDataHandler(store, geoip);
	}

	public void load(String event) {
		if(config.fileConfiguration.getBoolean("autoload", true) || event.equals("load")) {
			config.load();
			message.load();
			list.load();
		}
		separator = config.fileConfiguration.getString("separator", "%&%&");
		addGeoipFail();
		scheduleIntervals(event);
	}

	public void unload() {
		plugin.getServer().getScheduler().cancelTasks(plugin);
		running.clear();
		cycle.clear();
	}

	public void addGeoipFail() {
		geoipFail.clear();
		geoipFail.put("city", config.fileConfiguration.getString("cityfail", "Ragetown"));
		geoipFail.put("ccode", config.fileConfiguration.getString("ccodefail", "USL"));
		geoipFail.put("cname", config.fileConfiguration.getString("cnamefail", "United States of Lulz"));
		geoipFail.put("zip", config.fileConfiguration.getString("zipfail", "09001"));
		geoipFail.put("rcode", config.fileConfiguration.getString("rcodefail", "TF"));
		geoipFail.put("rname", config.fileConfiguration.getString("rnamefail", "Trollface"));
	}

	public void scheduleIntervals(String event) {
		Set<String> keys = getKeys(message.fileConfiguration, "messages.interval");
		if(keys != null && !event.equals("interval")) {
			for(String key : keys) {
				if(!running.contains(key)) {
					int interval = message.fileConfiguration.getInt("messages.interval." + key + ".interval", 300);
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

	public String getLocation(String type, String p) {
		return data.lookupGeoIP(PlayerDataHandler.getPlayer(p), type, geoipFail);
	}

	public String getTime(Long rawtime, boolean caps) {
		String day = config.fileConfiguration.getString("day");
		String dusk = config.fileConfiguration.getString("sunset");
		String dawn = config.fileConfiguration.getString("sunrise");
		String night = config.fileConfiguration.getString("night");

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
		return caps ? name : name.toLowerCase();
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
		String t = config.fileConfiguration.getString("istrue", "&2Yes");
		String f = config.fileConfiguration.getString("isfalse", "&4No");
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
		String online = config.fileConfiguration.getString("status.online", "&2Online");
		String offline = config.fileConfiguration.getString("status.offline", "&7Offline");
		String afk = config.fileConfiguration.getString("status.afk", "&6AFK");
		
		if (p.isOnline()) {
			status = caps ? online : online.toLowerCase();
			if (afkhandler.isActive()) {
				if (afkhandler.isAFK(plugin.getServer().getPlayerExact(p.getName()))) {
					status = afk;
				}
			}
		} else {
			status = caps ? offline : offline.toLowerCase();
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
			Set<String> keys = getKeys(list.fileConfiguration, "lists");
			if (MinecraftUtil.isSet(keys)) {
				for (String key : keys) {
					if (str.contains("%ol_" + key)) {
						String path = "lists." + key;
						boolean online = list.fileConfiguration.getBoolean(path + ".online", true);
						boolean formatted = list.fileConfiguration.getBoolean(path + ".formatted", false);
						List<String> groups = list.fileConfiguration.getStringList(path + ".players.groups");
						if (groups == null) {
							groups = new ArrayList<String>(pub);
						}
						List<String> users = getNonNullList(list.fileConfiguration.getStringList(path + ".players.users"));
						List<String> permissions = getNonNullList(list.fileConfiguration.getStringList(path + ".players.permissions"));
						List<String> worlds = getNonNullList(list.fileConfiguration.getStringList(path + ".players.worlds"));
						String format = list.fileConfiguration.getString(path + ".format", "%nm");
						String separator = list.fileConfiguration.getString(path + ".separator", ", ");
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
		SimpleDateFormat tf = new SimpleDateFormat(config.fileConfiguration.getString("timeformat", "K:mm a z"));
		SimpleDateFormat df = new SimpleDateFormat(config.fileConfiguration.getString("dateformat", "EEEE, MMMM d yyyy 'at' K:mm a z"));
		String[] onlineCodes = {
				"%world", "%rtime", "%time", "%Time", "%Mode", "%mode",
				"%asleep", "%Asleep", "%x", "%y", "%z", "%level",
				"%curxp", "%totalxp", "%food", "%exhaust", "%sat", "%ip",
				"%health"
				};
		String[] playerCodes = {
				"%firston", "%laston", "%nm", "%online", "%Online", "%status", "%Status", "%banned",
				"%Banned", "%white", "%White", "%op", "%Op", "%city", "%ccode", "%zip", "%rcode", "%rname"
		};
		
		if (isLeaveEvent(event)) {
			size = size - 1;
		}
		
		if(p != null) {
			str = str.replaceAll("%firston", df.format(p.getFirstPlayed()));
			str = str.replaceAll("%laston", getTimeDifference(p.getLastPlayed()));		
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
				str = str.replaceAll("%city", getLocation("city", p.getName()));
			}
			if (str.contains("%ccode")) {
				str = str.replaceAll("%ccode", getLocation("ccode", p.getName()));
			}
			if (str.contains("%cname")) {
				str = str.replaceAll("%cname", getLocation("cname", p.getName()));
			}
			if (str.contains("%zip")) {
				str = str.replaceAll("%zip", getLocation("zip", p.getName()));
			}
			if (str.contains("%rcode")) {
				str = str.replaceAll("%rcode", getLocation("rcode", p.getName()));
			}
			if (str.contains("%rname")) {
				str = str.replaceAll("%rname", getLocation("rname", p.getName()));
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
		str = str.replaceAll("%srtime", tf.format(Calendar.getInstance().getTime()));
		
		str = processColors(str);
		str = str.replaceAll("%sp", "");

		return textProcess(str);
	}

	public String onlineProcess(String str, Player p, String event, Map<String, String> args) {
		EconomyHandler economy = Main.getEconomy();
		PermissionsHandler permissions = Main.getPermissions();
		String ip = getIP(p);
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
		str = str.replaceAll("%curxp", Float.toString(p.getExp()));
		str = str.replaceAll("%totalxp", Integer.toString(p.getTotalExperience()));
		str = str.replaceAll("%food", p.getFoodLevel() == 20 ? "10" : Double.toString(p.getFoodLevel() / 2D));
		str = str.replaceAll("%exhaust", Float.toString(p.getExhaustion()));
		str = str.replaceAll("%sat", Float.toString(p.getSaturation()));
		str = str.replaceAll("%health", p.getHealth() == 20 ? "10" : Double.toString(p.getHealth() / 2D));
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

	public Player getPlayerFromNick(String nick) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (p.getDisplayName().equalsIgnoreCase(nick)) {
				return p;
			}
		}
		return null;
	}

	private static <T> List<T> getNonNullList(final List<T> rawList) {
		if (rawList == null) {
			return new ArrayList<T>(0);
		} else {
			return rawList;
		}
	}

	public Set<Entry> getEntries(Player trigger, String key, String event, String type) //For receivers/triggers
	{
		Set<Entry> entries = new HashSet<Entry>();
		final String keypath = "messages." + event + "." + key + "." + type;
		final String userpath = keypath + ".users";
		final String grouppath = keypath + ".groups";
		final String permspath = keypath + ".permissions";
		final String worldpath = keypath + ".worlds";
		for (String group : getNonNullList(message.fileConfiguration.getStringList(grouppath))) {
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

		for (String user : getNonNullList(message.fileConfiguration.getStringList(userpath))) {
			entries.add(new User(user));
		}

		for (String perm : getNonNullList(message.fileConfiguration.getStringList(permspath))) {
			entries.add(new Permission(perm, Main.getPermissions(), plugin));
		}
		
		for (String world : getNonNullList(message.fileConfiguration.getStringList(worldpath))) {
			entries.add(new com.tahkeh.loginmessage.entries.World(world, plugin));
		}
		return entries;
	}
	
	public String getIP(Player p) {
		return data.getIP(p);
	}
	

	private static Set<String> getKeys(final ConfigurationSection section, final String path) {
		ConfigurationSection subSection = section.getConfigurationSection(path);
		if (subSection != null) {
			return subSection.getKeys(false);
		} else {
			return null;
		}
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
			Set<String> keyList = getKeys(message.fileConfiguration, "messages." + event);
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

	public static double toDouble(final Object object, final double def) {
		if (object instanceof Number) {
			return ((Number) object).doubleValue();
		}

		try {
			return Double.valueOf(object.toString());
		} catch (NumberFormatException e) {
		} catch (NullPointerException e) {
		}
		return def;
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
		List<Map<?,?>> mapList = this.message.fileConfiguration.getMapList("messages." + event + "." + name + ".messages");
		String[] lines = EMPTY_STRING_ARRAY;
		if (MinecraftUtil.isSet(mapList)) {
			// See: MinecraftUtil.getRandomFromChances
			// Read chances
			int length = mapList.size();
			double totalchance = 0;
			double defChance = 1.0 / mapList.size();
			for (Map<?, ?> messageNode : mapList) {
				totalchance += toDouble(messageNode.get("chance"), defChance);
			}
			double value = Math.random() * totalchance;

			if (mapList.get(0).get("order") != null) {
				int idx;
				if (!cycle.containsKey(name)) {
					cycle.put(name, 0);
					idx = 0;
				} else {
					idx = cycle.get(name);
					if (idx >= mapList.size()) {
						idx = 0;
						cycle.remove(name);
					}
				}
				lines = getStringList(mapList.get(idx).get("order"), EMPTY_STRING_ARRAY);
				cycle.put(name, cycle.get(name) >= length - 1 ? 0 : cycle.get(name) + 1);
			} else {
				for (Map<?, ?> messageNode : mapList) {
					value -= toDouble(messageNode.get("chance"), defChance);
					if (value < 0) {
						lines = getStringList(messageNode.get("random"), EMPTY_STRING_ARRAY);
						break;
					}
				}
			}
		} else {
			lines = getStringList(message.fileConfiguration, "messages." + event + "." + name + ".message", EMPTY_STRING_ARRAY);
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
	public static String[] getStringList(ConfigurationSection node, String path, String[] def) {
		Object property = node.get(path);
		return getStringList(property, def);
	}

	public static String[] getStringList(final Object property, final String[] def) {
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

	public void finishMessage(OfflinePlayer p, String event, String key, Map<String, String> args) // Final touches - delay and cooldown
	{
		String[] lines = this.getLines(event, key);
		if (lines.length == 0) {
			this.logger.info("Empty message named '" + key + "' (Event: '" + event + "') found.");
		} else {
			final String keypath = "messages." + event + "." + key;
			int cd = message.fileConfiguration.getInt(keypath + ".cooldown", 0) * 1000;
			int dl = message.fileConfiguration.getInt(keypath + ".delay", 0);

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
		String[] processedLines = new String[lines.length];
		for (int i = 0; i < processedLines.length; i++) {
			processedLines[i] = processLine(lines[i], trigger, event, args);
		}
		for (Player receiver : possibleReceivers) {
			for (String processedLine : processedLines) {
				// Check again for an empty line after all the codes have been processed.
				if (!processedLine.isEmpty()) {
					receiver.sendMessage(processedLine);
				}
			}
		}
		if (task != null) {
			task.trigger();
		}
	}

	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		String e = p.hasPlayedBefore() ? "login" : "firstlogin";
		load(e);
		preProcessMessage(p, e, null);
		
		if (config.fileConfiguration.getBoolean("clearjoinmsg", true)) {
			event.setJoinMessage(null);
		}
	}

	public void onPlayerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (!this.kicked.remove(p.getName())) {
			load("quit");
			preProcessMessage(p, "quit", null);

			if (config.fileConfiguration.getBoolean("clearquitmsg", true)) {
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

		if (config.fileConfiguration.getBoolean("clearkickmsg", true)) {
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
		Set<String> commands = getKeys(message.fileConfiguration, "messages.command");
		String npf = config.fileConfiguration.getString("noplayerfound", "&cPlayer \"%nm\" does not exist!");
		
		args.put("cmd", cmdargs[0]);
		if (commands != null) {
			for (String key : commands) {
				List<String> msgargs = getNonNullList(message.fileConfiguration.getStringList("messages.command." + key + ".args"));
				if (key.equalsIgnoreCase(cmdargs[0])) {
					event.setCancelled(true);
					if (cmdargs.length >= 1) {
						if (cmdargs.length >= 2) {
							npf = npf.replaceAll("%nm", cmdargs[1]);
							npf = processColors(npf);
							OfflinePlayer target = null;
							if (msgargs.contains("pname")) {
								if (Bukkit.getPlayerExact(cmdargs[1]) != null) {
									target = Bukkit.getPlayerExact(cmdargs[1]);
								}
							}
							if (msgargs.contains("pdname")) {
								if (getPlayerFromNick(cmdargs[1]) != null) {
									target = getPlayerFromNick(cmdargs[1]);
								}
							}
							if (msgargs.contains("poname")) {
								if (Bukkit.getOfflinePlayer(cmdargs[1]).hasPlayedBefore() && !Bukkit.getOfflinePlayer(cmdargs[1]).isOnline()) {
									target = Bukkit.getOfflinePlayer(cmdargs[1]);
								}
							}
							if (target == null) {
								sender.sendMessage(npf);
							} else {
								args.put("trigger", sender.getName());
								preProcessMessage(target, "command", args);
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
		Set<String> keys = getKeys(message.fileConfiguration, "messages.death");
		
		if (keys != null) {
			for (String key : keys) {
				List<String> triggerCauses = getNonNullList(message.fileConfiguration.getStringList("messages.death." + key + ".causes"));
				Set<Cause> possibleCauses = handler.getCauses();
				if (triggerCauses != null) {
					keyCauses.put(key, triggerCauses);
				}
				if (DeathHandler.matchCauses(keyCauses.get(key), possibleCauses)) {
					args.put("key", key);
					args.put("entity", handler.getKiller());
					args.put("item", handler.isKillerPlayer() ? handler.getItem(plugin.getServer().getPlayerExact(handler.getKiller()).getItemInHand()) : "?");
					preProcessMessage(p, "death", args);
				}
			}
		}
		
		if (config.fileConfiguration.getBoolean("cleardeathmsg", true)) {
			event.setDeathMessage(null);
		}
	}
}
