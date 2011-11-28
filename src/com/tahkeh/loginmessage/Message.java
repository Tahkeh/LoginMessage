package com.tahkeh.loginmessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
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

import javax.script.ScriptEngineManager;

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
import com.tahkeh.loginmessage.methods.AliasMethod;
import com.tahkeh.loginmessage.methods.MethodParser;
import com.tahkeh.loginmessage.methods.ScriptMethod;
import com.tahkeh.loginmessage.methods.impl.AsleepMethod;
import com.tahkeh.loginmessage.methods.impl.BannedMethod;
import com.tahkeh.loginmessage.methods.impl.CurrentExperienceMethod;
import com.tahkeh.loginmessage.methods.impl.DisplayNameMethod;
import com.tahkeh.loginmessage.methods.impl.ExhaustMethod;
import com.tahkeh.loginmessage.methods.impl.FoodLevelMethod;
import com.tahkeh.loginmessage.methods.impl.GameModeMethod;
import com.tahkeh.loginmessage.methods.impl.GroupNameMethod;
import com.tahkeh.loginmessage.methods.impl.IPMethod;
import com.tahkeh.loginmessage.methods.impl.IndefiniteArticleMethod;
import com.tahkeh.loginmessage.methods.impl.LastLoginMethod;
import com.tahkeh.loginmessage.methods.impl.LevelMethod;
import com.tahkeh.loginmessage.methods.impl.LocationMethod;
import com.tahkeh.loginmessage.methods.impl.MaximumPlayersMethod;
import com.tahkeh.loginmessage.methods.impl.NameMethod;
import com.tahkeh.loginmessage.methods.impl.OnlineMethod;
import com.tahkeh.loginmessage.methods.impl.OpMethod;
import com.tahkeh.loginmessage.methods.impl.PermissionMethod;
import com.tahkeh.loginmessage.methods.impl.RawTimeMethod;
import com.tahkeh.loginmessage.methods.impl.RealLocationMethod;
import com.tahkeh.loginmessage.methods.impl.SaturationMethod;
import com.tahkeh.loginmessage.methods.impl.ServerTimeMethod;
import com.tahkeh.loginmessage.methods.impl.SizeMethod;
import com.tahkeh.loginmessage.methods.impl.StatusMethod;
import com.tahkeh.loginmessage.methods.impl.TimeMethod;
import com.tahkeh.loginmessage.methods.impl.TotalExperienceMethod;
import com.tahkeh.loginmessage.methods.impl.WhitelistedMethod;
import com.tahkeh.loginmessage.methods.impl.WorldMethod;
import com.tahkeh.loginmessage.methods.variables.DeathVariables;
import com.tahkeh.loginmessage.methods.variables.KickVariables;
import com.tahkeh.loginmessage.methods.variables.DefaultVariables;
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
	private final MethodParser methodParser;

	String separator = "%&%&";
	Set<String> kicked = new HashSet<String>();
	List<String> running = new ArrayList<String>();
	Map<String, Integer> cycle = new HashMap<String, Integer>();
	
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
		this.methodParser = new MethodParser(logger, "%");
		// Register methods
		}

	public void load(String event) {
		if(config.getBoolean("autoload", true) || event.equals("load")) {
			config.load();
			message.load();
			list.load();

			this.methodParser.clearMethods();
			this.methodParser.loadDefaults();

			/*
			 * Load original methods
			 */
			// OfflinePlayer methods
			new LastLoginMethod(this).register("laston", this.methodParser);
			new NameMethod().register("nm", this.methodParser);
			new StatusMethod(this).register("status", this.methodParser);
			new OnlineMethod(this).register("online", this.methodParser);
			new BannedMethod(this).register("banned", this.methodParser);
			new WhitelistedMethod(this).register("white", this.methodParser);
			new OpMethod(this).register("op", this.methodParser);
			new RealLocationMethod("city", this).register(this.methodParser);
			new RealLocationMethod("ccode", this).register(this.methodParser);
			new RealLocationMethod("cname", this).register(this.methodParser);
			new RealLocationMethod("zip", this).register(this.methodParser);
			new RealLocationMethod("rcode", this).register(this.methodParser);
			new RealLocationMethod("rname", this).register(this.methodParser);

			// Player methods
			new DisplayNameMethod().register("dpnm", this.methodParser);
			new WorldMethod().register("world", this.methodParser);
			new RawTimeMethod().register("rtime", this.methodParser);
			new TimeMethod(this).register("time", this.methodParser);
			new GameModeMethod().register("mode", this.methodParser);
			new AsleepMethod(this).register("asleep", this.methodParser);
			new LocationMethod().register("location", this.methodParser);
			/* Following methods are replaced by the location method:
			 * str = str.replaceAll("%x", Integer.toString(p.getLocation().getBlockX()));
			 * str = str.replaceAll("%y", Integer.toString(p.getLocation().getBlockY()));
			 * str = str.replaceAll("%z", Integer.toString(p.getLocation().getBlockZ()));
			 */
			new LevelMethod().register("level", this.methodParser);
			new CurrentExperienceMethod().register("curxp", this.methodParser);
			new TotalExperienceMethod().register("totalxp", this.methodParser);
			new FoodLevelMethod().register("food", this.methodParser);
			new ExhaustMethod().register("exhaust", this.methodParser);
			new SaturationMethod().register("sat", this.methodParser);
			new IPMethod(this).register("ip", this.methodParser);
			new GroupNameMethod(Main.getPermissions(), this.logger).register("group", this.methodParser);
			new PermissionMethod<String>(Main.getPermissions(), PREFIX_PERMISSION, PermissionMethod.PLAYER_STRING_CALLBACK).register("prefix", this.methodParser);
			new PermissionMethod<String>(Main.getPermissions(), SUFFIX_PERMISSION, PermissionMethod.PLAYER_STRING_CALLBACK).register("suffix", this.methodParser);

			// Methods without any relation to players
			new IndefiniteArticleMethod().register("an", this.methodParser);
			new SizeMethod().register("size", this.methodParser);
			new MaximumPlayersMethod().register("max", this.methodParser);
			new ServerTimeMethod(this).register("srtime", this.methodParser);

			Configuration configuration = new Configuration(new File(this.plugin.getDataFolder(), "methods.yml"));
			configuration.load();
			Map<String, ConfigurationNode> scriptNodes = configuration.getNodes("script");
			int scriptEnabled = 0;
			if (scriptNodes != null) {
				ScriptEngineManager engineManager = new ScriptEngineManager();
				for (Map.Entry<String, ConfigurationNode> scriptNode : scriptNodes.entrySet()) {
					String name = scriptNode.getKey();
					ConfigurationNode values = scriptNode.getValue();

					String method = values.getString("method", "call");
					boolean recursive = values.getBoolean("recursive", true);
					String engine = values.getString("engine");
					String fileName = values.getString("file");
					int[] paramCountArray = getParamCounts(values, "parameters", 0, -1);
					Reader reader = null;
					if (fileName != null) {
						try {
							reader = new FileReader(fileName);
						} catch (FileNotFoundException e) {
							this.logger.warning("Script file not found: " + fileName);
							reader = null;
						}
					} else {
						String code = values.getString("code");
						if (code != null) {
							reader = new StringReader(code);
						} else {
							this.logger.warning("No script code available.");
						}
					}

					if (engine == null) {
						this.logger.warning("No engine defined.");
					} else if (reader != null) {
						ScriptMethod methodObj = ScriptMethod.create(engine, method, reader, engineManager, recursive, logger);
						if (methodObj != null) {
							this.methodParser.registerMethod(name, methodObj, paramCountArray);
							scriptEnabled++;
						}
						try {
							reader.close();
						} catch (IOException e) {
							this.logger.warning("Unable to close the script reader.");
						}
					} else {
						this.logger.warning("Unable to add script named '" + name + "'!");
					}
				}
			}

			Map<String, ConfigurationNode> aliasNodes = configuration.getNodes("alias");
			int aliasEnabled = 0;
			if (aliasNodes != null) {
				for (Map.Entry<String, ConfigurationNode> aliasNode : aliasNodes.entrySet()) {
					String name = aliasNode.getKey();
					ConfigurationNode values = aliasNode.getValue();
					String calls = values.getString("call");
					int paramCount = values.getInt("parameters", 0);
					this.methodParser.registerMethod(name, new AliasMethod(calls, paramCount), paramCount);
					aliasEnabled++;
				}
			}

			Map<String, ConfigurationNode> redirectedNodes = configuration.getNodes("redirect");
			int redirectedEnabled = 0;
			if (redirectedNodes != null) {
				List<MethodParser.RedirectedElement> redirectedElements = new ArrayList<MethodParser.RedirectedElement>(redirectedNodes.size() * 2);
				for (Map.Entry<String, ConfigurationNode> redirectedNode : redirectedNodes.entrySet()) {
					String name = redirectedNode.getKey();
					ConfigurationNode values = redirectedNode.getValue();
					String calls = values.getString("call");
					int[] paramCountArray = getParamCounts(values, "parameters", 0, -1);
					for (int paramCount : paramCountArray) {
						redirectedElements.add(new MethodParser.RedirectedElement(name, calls, paramCount));
					}
					redirectedEnabled++;
				}
				this.methodParser.createRedirected(redirectedElements);
			}
			this.logger.info("Registered " + (redirectedEnabled + aliasEnabled + scriptEnabled) + " user defined methods (" + scriptEnabled + " scripts, " + aliasEnabled + " aliases, " + redirectedEnabled + " redirects)!");
		}
		separator = config.getString("separator", "%&%&");
		store.load(event);
		scheduleIntervals(event);
	}

	private static int[] getParamCounts(final ConfigurationNode node, final String name, final int... counts) {
		List<Integer> parameterCounts = node.getIntList(name, null);
		if (parameterCounts.size() == 0) {
			parameterCounts = new ArrayList<Integer>();
			for (Integer integer : counts) {
				parameterCounts.add(integer);
			}
		}
		int[] paramCountArray = new int[parameterCounts.size()];
		int idx = 0;
		for (Integer integer : parameterCounts) {
			paramCountArray[idx++] = integer;
		}
		return paramCountArray;
	}

	public SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat(config.getString("format", "K:mm a z"));
	}

	public void unload() {
		plugin.getServer().getScheduler().cancelTasks(plugin);
		running.clear();
		cycle.clear();
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
		
		if (difference == 0) {
			lines.add("a moment");
		} else {
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
			}
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
		String name = getTime(rawtime);
		if (name == null) {
			name = "";
		}
		return caps ? toCapitalCase(name) : name.toLowerCase();
	}

	public String getTime(Long rawtime) {
		int modTime = (int) (rawtime % 24000);

		if (modTime == 24000 || modTime <= 11999) {
			return config.getString("day");
		} else if (modTime == 12000 || modTime <= 12999) {
			return config.getString("sunset");
		} else if (modTime == 13000 || modTime <= 22999) {
			return config.getString("night");
		} else if (modTime == 23000 || modTime <= 23999) {
			return config.getString("sunrise");
		} else {
			return null;
		}
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

	public static String getPrefix(String group, String world) {
		return Main.getPermissions().getString(world, group, PREFIX_PERMISSION);
	}

	public static String getSuffix(String group, String world) {
		return Main.getPermissions().getString(world, group, SUFFIX_PERMISSION);
	}

	public static boolean isLeaveEvent(String event) // For %ol and %size
	{
		return event.equals("kick") || event.equals("quit");
	}

	public String booleanToName(boolean bool, boolean caps) {
		String name = booleanToName(bool);

		if (name == null) {
			name = "";
		}
		
		name = processColors(name);

		return caps ? name : name.toLowerCase();
	}
	
	public String booleanToName(boolean bool) {
		return bool ? config.getString("istrue", "&2Yes") : config.getString("isfalse", "&4No");
	}
	
	public String getStatus(OfflinePlayer p, boolean caps) {
		String status = getStatus(p);
		return processColors(caps ? toCapitalCase(status) : status.toLowerCase());
	}

	public String getStatus(OfflinePlayer p) {
		AFKHandler afkhandler = new AFKHandler(plugin);
		final String status;

		if (p.isOnline()) {
			if (afkhandler.isActive() && afkhandler.isAFK(plugin.getServer().getPlayerExact(p.getName()))) {
				status = config.getString("status.afk", "&6AFK");
			} else {
				status = config.getString("status.online", "&2Online");
			}
		} else {
			status = config.getString("status.offline", "&7Offline");
		}
		
		return status;
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

	private static DefaultVariables generateVariables(final OfflinePlayer player, final String event, final Map<String, String> args) {
		DefaultVariables variables = null;
		Player trigger = null;
		if (player != null) {
			trigger = Bukkit.getServer().getPlayerExact(player.getName());
			if (trigger == null && !event.equals("list") && args.containsKey("trigger")) {
				trigger = Bukkit.getServer().getPlayerExact(args.get("trigger"));
			}
		} else {
			trigger = null;
		}
		if (event.equalsIgnoreCase("kick")) {
			String reason = args.get("kickreason");
			if (reason != null) {
				variables = new KickVariables(reason, trigger);
			}
		} else if (event.equalsIgnoreCase("death")) {
			String item = args.get("item");
			String entity = args.get("entity");
			if (item != null && entity != null) {
				variables = new DeathVariables(item, entity, trigger);
			}
		}
		return variables == null ? new DefaultVariables(trigger) : variables;
	}

	public String processLine(String str, OfflinePlayer p, String event, DefaultVariables variables) {
		Player trigger = variables.trigger;

		str = this.methodParser.parseLine(p, event, str, variables);
		if (!event.equals("list")) {
			str = processOnlineList(str, trigger, event);
		}
		
		str = processColors(str);
		str = str.replaceAll("%sp", "");

		return str;
	}

	public long getLastLogin(String name) {
		return this.store.getLastLogin(name);
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
			int length = messages.toArray().length;
			double totalchance = 0;
			double defChance = 1.0 / messages.size();
			for (ConfigurationNode messageNode : messages) {
				totalchance += messageNode.getDouble("chance", defChance);
			}
			
			double value = Math.random() * totalchance;
			if (messages.get(0).getProperty("order") != null) {
				if (!cycle.containsKey(name)) {
					cycle.put(name, 0);
				}
				lines = getStringList(messages.get(cycle.get(name)), "order", EMPTY_STRING_ARRAY);
				cycle.put(name, cycle.get(name) >= length - 1 ? 0 : cycle.get(name) + 1);
			} else {
				for (ConfigurationNode messageNode : messages) {
					value -= messageNode.getDouble("chance", defChance);
					if (value < 0) {
						lines = getStringList(messageNode, "random", EMPTY_STRING_ARRAY);
						break;
					}
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
		DefaultVariables variables = generateVariables(trigger, event, args);
		ArrayList<String> processedLines = new ArrayList<String>(lines.length);
		for (int i = 0; i < lines.length; i++) {
			String processedLine = processLine(lines[i], trigger, event, variables);
			if (processedLine != null && !processedLine.trim().isEmpty()) {
				processedLines.add(processedLine);
			}
		}

		for (Player receiver : possibleReceivers) {
			for (String processedLine : processedLines) {
				receiver.sendMessage(processedLine);
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