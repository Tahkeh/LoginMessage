package com.tahkeh.loginmessage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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
	private final XLogger logger;
	private final Store store;

	String separator = "%&%&";
	boolean cont = true;
	List<String> running = new ArrayList<String>();
	
	private final Cooldown cooldown;
	
	public Message(Main instance, Configuration config, Configuration message, XLogger logger, Store store)
	{
		this.plugin = instance;
		this.config = config;
		this.message = message;
		this.logger = logger;
		this.cooldown = new Cooldown();
		this.store = store;
		}
	
	public void load(String event) {
		if(config.getBoolean("autoload", true) || event.equals("load")) {
			config.load();
			message.load();
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
					int interval = message.getInt("messages.interval." + key + ".interval", 0);
					plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Interval(this, key), 0, interval * 20);
					running.add(key);
				}
			}
		}
	}

	public String getTimeDifference(long start) {
		Date end = Calendar.getInstance().getTime();
		long difference = (end.getTime() - start) / 1000;
		long date[] = new long[] {0, 0, 0, 0};
		StringBuilder sb = new StringBuilder();
		
		date[3] = (difference >= 60 ? difference % 60 : difference);
		date[2] = (difference = (difference / 60)) >= 60 ? difference % 60 : difference;
		date[1] = (difference = (difference / 60)) >= 24 ? difference % 24 : difference;
		date[0] = (difference = (difference / 24));
		
		if (date[0] > 0) {
			sb.append(String.format("%d day%s", date[0], date[0] != 1 ? "s" : ""));
		}
		if (date[1] > 0) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(String.format("%d hour%s", date[1], date[1] != 1 ? "s" : ""));
		}
		if (date[2] > 0) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(String.format("%d minute%s", date[2], date[2] != 1 ? "s" : ""));
		}
		if (date[3] > 0) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(String.format("%d second%s", date[3], date[3] != 1 ? "s" : ""));
		} else if (difference == 0) {
			sb.append("a moment");
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
	
	public String getTime(Long rawtime, boolean caps) //Neat little method to get the text-based version of the time!
	{
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
		return caps ? name : name.toLowerCase();
	}
	
	public String getGameMode(GameMode mode, boolean caps)
	{
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
		String t = config.getString("istrue", "Yes");
		String f = config.getString("isfalse", "No");
		String name = "";

		if (bool) {
			name = t;
		} else {
			name = f;
		}
		
		name = name.replaceAll("(&([a-z0-9]))", SECTION_SIGN + "$2");

		return caps ? name : name.toLowerCase();
	}
	
	public String textProcess(String str) {
		boolean vowel = false;
		if(str.contains("%an%")) {
			String code = str.substring(str.indexOf("%an%"), str.indexOf("%an%") + 4);
			String letter = str.substring(str.indexOf("%an%") + 5, str.indexOf("%an%") + 6);
			if(letter.equalsIgnoreCase("a") || letter.equalsIgnoreCase("e") || letter.equalsIgnoreCase("i") || letter.equalsIgnoreCase("o") || letter.equalsIgnoreCase("u")) {
				vowel = true;
			}
			if(vowel) {
				str = str.replace(code, "an");
			} else {
				str = str.replace(code, "a");
			}
		}
		
		return str;
	}
	
	public String processOnlineList(String str, Player p, String event) {
		PermissionsHandler handler = Main.getPermissions();
		if(str.contains("%ol" + separator)) {
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
			for(Player all : players) {
				while(!online.contains(all)) {
					online.add(all);
				}
				if(isLeaveEvent(event)) {
					online.remove(p);
					length = length - 1;
				}
			}
			if(s.substring(code.length() - 1, s.indexOf(":")).length() == 1 || s.substring(code.length() - 1, s.indexOf(":")).length() == 0) {
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
		        	prefix = prefix.replaceAll("(&([a-z0-9]))", SECTION_SIGN + "$2");
		        	suffix = suffix.replaceAll("(&([a-z0-9]))", SECTION_SIGN + "$2");
		        	list = list + (on >= length ? prefix + name + suffix : prefix + name + suffix + ", ");
		        	on++;
		        }
		        String ol = code + a + ":" + b + ":" + c;
		        str = str.replaceAll(ol, list);
			}
		}
		if(str.contains("%ol_")) {
		}
		if(str.contains("%ol")) {
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
				"%laston", "%nm", "%status", "%Status", "%banned", "%Banned",
				"%white", "%White", "%op", "%Op", "%city", "%ccode", "%zip",
				"%rcode", "%rname"
		};
		
		if (isLeaveEvent(event)) {
			size = size - 1;
		}
		
		if(p != null) {
			str = str.replaceAll("%laston", getTimeDifference(store.getLastLogin(p.getName())));		
			str = str.replaceAll("%nm", p.getName());
			str = str.replaceAll("%status", booleanToName(p.isOnline(), false));
			str = str.replaceAll("%Status", booleanToName(p.isOnline(), true));
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
			} else {
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
		if (args == null || !args.containsKey("listprocess")) {
			// We don't want this method to keep calling itself, so we
			// use arguments to check if the processLine method was called by it
			str = processOnlineList(str, trigger, event);
		}
		str = str.replaceAll("%size", Integer.toString(size));
		str = str.replaceAll("%max", Integer.toString(plugin.getServer().getMaxPlayers()));
		str = str.replaceAll("%srtime", sdf.format(Calendar.getInstance().getTime()));
		
		str = str.replaceAll("(&([a-z0-9]))", SECTION_SIGN + "$2");
		str = str.replaceAll("%sp", "");

		return textProcess(str);
	}

	public String onlineProcess(String str, Player p, String event, Map<String, String> args) {
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
				entries.add(new Group(group, Main.getPermissions()));
			}
		}

		for (String user : message.getStringList(userpath, null)) {
			entries.add(new User(user));
		}

		for (String perm : message.getStringList(permspath, null)) {
			entries.add(new Permission(perm, Main.getPermissions()));
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
			List<String> msgargs = message.getStringList("messages." + event + "." + key + ".args", null);
			Set<Entry> triggers = null;
			if (msgargs == null || !msgargs.contains("player")) {
				Player onlinetrigger = plugin.getServer().getPlayerExact(trigger.getName());
				triggers = getEntries(onlinetrigger, key, event, "triggers");
				if (matchEntries(onlinetrigger, triggers)) {
					finishMessage(trigger, event, key, args);
				}
			} else {
				finishMessage(trigger, event, key, args);
			}
		}
	}

	private static boolean matchEntries(Player player, Collection<Entry> entries) {
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

	public void finishMessage(OfflinePlayer p, String event, String key, Map<String, String> args) // Final touches - delay and cooldown
	{
		final String keypath = "messages." + event + "." + key;
		int cd = message.getInt(keypath + ".cooldown", 0) * 1000;
		int dl = message.getInt(keypath + ".delay", 0);

		Player[] players = this.plugin.getServer().getOnlinePlayers();
		List<Player> cooledDown = new ArrayList<Player>(players.length);
		CooldownTask task = null;

		// Since the only number lower than 1000 that is possible for the
		// cooldown is 0, we make sure it isn't for the same reason as the delay.
		if (cd > 0) {
			List<String> cdstrs = new ArrayList<String>(players.length);
			for (Player player : players) {
				if (this.cooldown.isCooledDown(player, key, event)) {
					cooledDown.add(player);
					cdstrs.add(Cooldown.createKey(player, key, event));
				}
			}
			task = this.cooldown.createTask(cdstrs, this.cooldown, cd);
		} else {
			cooledDown.addAll(Arrays.asList(players));
			task = null;
		}

		if (cooledDown.size() > 0) {
			// Check if the delay isn't greater than or equal to 3.
			// Anything below 3 milliseconds makes your computer sad from my experience.
			if (dl >= 3) {
				new Timer().schedule(new Delay(this, key, p, event, cooledDown, task, args), dl);
			} else {
				sendMessage(p, cooledDown, key, event, task, args);
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
	public void sendMessage(OfflinePlayer trigger, Collection<Player> possibleReceivers, String key, String event, CooldownTask task, Map<String, String> args)
	{
		Player truetrigger = null;
		if (event.equals("command") && args.containsKey("trigger")) {
			truetrigger = plugin.getServer().getPlayerExact(args.get("trigger"));
		} else if (trigger != null) {
			truetrigger = plugin.getServer().getPlayerExact(trigger.getName());
		}
		Set<Entry> receivers = getEntries(truetrigger, key, event, "receivers");
		String[] lines = this.getLines(event, key);
		if (lines.length > 0) {
			for (Player receiver : possibleReceivers) {
				if (matchEntries(receiver, receivers)) {
					for (String str : lines) {
						receiver.sendMessage(processLine(str, trigger, event, args));
					}
				}
			}
		} else {
			this.logger.info("Empty message named '" + key + "' (Event: '" + event + "') found.");
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
		if (cont) {
			load("quit");
			Player p = event.getPlayer();
			preProcessMessage(p, "quit", null);

			if (config.getBoolean("clearquitmsg", true)) {
				event.setQuitMessage(null);
			}
		} else {
			cont = true;
		}
	}

	public void onPlayerKick(PlayerKickEvent event) {
		cont = false;
		load("kick");
		Player p = event.getPlayer();
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
								String noplayer = config.getString("noplayerfound", "&cPlayer \"%nm\" does not exist!");
								noplayer = noplayer.replaceAll("%nm", cmdargs[1]);
								noplayer = noplayer.replaceAll("(&([a-z0-9]))", SECTION_SIGN + "$2");
								sender.sendMessage(noplayer);
							}
						} else {
							preProcessMessage(sender, "command", args);
						}
					}
				}
			}
		}
	}
	
	public void onPlayerDeath(PlayerDeathEvent event, Map<String, String> args) {
		load("death");
		
		
		if (config.getBoolean("cleardeathmsg", true)) {
			event.setDeathMessage(null);
		}
	}
}