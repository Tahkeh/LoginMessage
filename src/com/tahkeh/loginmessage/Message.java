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
import com.tahkeh.loginmessage.methods.MethodParser;
import com.tahkeh.loginmessage.methods.impl.AliasMethod;
import com.tahkeh.loginmessage.methods.impl.ScriptMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.AsleepMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.BannedMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.CurrentExperienceMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.DeathEntityMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.DeathItemMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.DecimalTimeMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.DisplayNameMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.ExhaustMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.FoodLevelMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.GameModeMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.GroupNameMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.IPMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.LastLoginMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.LevelMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.LocationMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.MaximumPlayersMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.NameMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.OnlineMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.OnlistMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.OpMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.PermissionMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.RawTimeMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.RealLocationMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.SaturationMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.ServerTimeMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.SizeMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.StatusMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.TargetPlayerMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.TimeMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.TotalExperienceMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.WhitelistedMethod;
import com.tahkeh.loginmessage.methods.impl.bukkit.WorldMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.CommandVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.DeathVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.KickVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerNotFoundVariables;
import com.tahkeh.loginmessage.store.Store;
import com.tahkeh.loginmessage.timers.Cooldown;
import com.tahkeh.loginmessage.timers.Delay;
import com.tahkeh.loginmessage.timers.Cooldown.CooldownTask;
import com.tahkeh.loginmessage.timers.Interval;

import de.xzise.XLogger;
import de.xzise.wrappers.permissions.BufferPermission;

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
	private final MethodParser<BukkitVariables> methodParser;

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
		this.methodParser = new MethodParser<BukkitVariables>(logger, "%");
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
			new LastLoginMethod(this).register(this.methodParser);
			new NameMethod().register(this.methodParser);
			new StatusMethod(this).register(this.methodParser);
			new OnlineMethod(this).register(this.methodParser);
			new BannedMethod(this).register(this.methodParser);
			new WhitelistedMethod(this).register(this.methodParser);
			new OpMethod(this).register(this.methodParser);
			new RealLocationMethod("city", this).register(this.methodParser);
			new RealLocationMethod("ccode", this).register(this.methodParser);
			new RealLocationMethod("cname", this).register(this.methodParser);
			new RealLocationMethod("zip", this).register(this.methodParser);
			new RealLocationMethod("rcode", this).register(this.methodParser);
			new RealLocationMethod("rname", this).register(this.methodParser);
			new OnlistMethod(this).register(this.methodParser);
			new TargetPlayerMethod().register(this.methodParser);

			// Player methods
			new DisplayNameMethod().register(this.methodParser);
			new WorldMethod().register(this.methodParser);
			new RawTimeMethod().register(this.methodParser);
			new TimeMethod(this).register(this.methodParser);
			new DecimalTimeMethod(this).register(this.methodParser);
			new GameModeMethod(this).register(this.methodParser);
			new AsleepMethod(this).register(this.methodParser);
			new LocationMethod().register(this.methodParser);
			/* Following methods are replaced by the location method:
			 * str = str.replaceAll("%x", Integer.toString(p.getLocation().getBlockX()));
			 * str = str.replaceAll("%y", Integer.toString(p.getLocation().getBlockY()));
			 * str = str.replaceAll("%z", Integer.toString(p.getLocation().getBlockZ()));
			 */
			new LevelMethod().register(this.methodParser);
			new CurrentExperienceMethod().register(this.methodParser);
			new TotalExperienceMethod().register(this.methodParser);
			new FoodLevelMethod().register(this.methodParser);
			new ExhaustMethod().register(this.methodParser);
			new SaturationMethod().register(this.methodParser);
			new IPMethod(this).register(this.methodParser);
			new GroupNameMethod(Main.getPermissions(), this.logger).register(this.methodParser);
			new PermissionMethod<String>(Main.getPermissions(), PREFIX_PERMISSION, PermissionMethod.PLAYER_STRING_CALLBACK, "prefix").register(this.methodParser);
			new PermissionMethod<String>(Main.getPermissions(), SUFFIX_PERMISSION, PermissionMethod.PLAYER_STRING_CALLBACK, "suffix").register(this.methodParser);
			new DeathEntityMethod().register(this.methodParser);
			new DeathItemMethod().register(this.methodParser);

			// Methods without any relation to players
			new SizeMethod().register(this.methodParser);
			// This doesn't collide with the maximum method, as this requests no parameters!
			new MaximumPlayersMethod().register(this.methodParser);
			new ServerTimeMethod(this).register(this.methodParser);

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
					if (calls != null) {
						new AliasMethod(calls, paramCount, name).register(this.methodParser);
						aliasEnabled++;
					} else {
						this.logger.warning("No call definition for alias method '" + name + "'!");
					}
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
					if (calls != null) {
						for (int paramCount : paramCountArray) {
							redirectedElements.add(new MethodParser.RedirectedElement(name, calls, paramCount));
						}
						redirectedEnabled++;
					} else {
						this.logger.warning("No call definition for redirected method '" + name + "'!");
					}
				}
				this.methodParser.createRedirected(redirectedElements);
			}
			this.logger.info("Registered " + (redirectedEnabled + aliasEnabled + scriptEnabled) + " user defined methods (" + scriptEnabled + " scripts, " + aliasEnabled + " aliases, " + redirectedEnabled + " redirects)!");
		}
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

	public SimpleDateFormat getDateFormat(final String name, final String defaultFormat) {
		return new SimpleDateFormat(this.config.getString("format." + name, defaultFormat));
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

	public String getLocation(String type, String p) {
		return store.getLocation(type, p);
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

	public static String getDefaultGameModeText(final GameMode mode) {
		switch (mode) {
		case CREATIVE :
			return "Creative";
		case SURVIVAL :
			return "Survival";
		default :
			return null;
		}
	}

	public String getGameModeText(final GameMode mode) {
		return this.config.getString("mode." + mode.getValue(), getDefaultGameModeText(mode));
	}

	public static String getPrefix(String group, String world) {
		return Main.getPermissions().getString(world, group, PREFIX_PERMISSION);
	}

	public static String getSuffix(String group, String world) {
		return Main.getPermissions().getString(world, group, SUFFIX_PERMISSION);
	}

	public String booleanToName(boolean bool) {
		return bool ? config.getString("istrue", "&2Yes") : config.getString("isfalse", "&4No");
	}

	public String getStatus(OfflinePlayer p) {
		if (p.isOnline()) {
			AFKHandler afkhandler = new AFKHandler(plugin);
			if (afkhandler.isActive() && afkhandler.isAFK(plugin.getServer().getPlayerExact(p.getName()))) {
				return config.getString("status.afk", "&6AFK");
			} else {
				return config.getString("status.online", "&2Online");
			}
		} else {
			return config.getString("status.offline", "&7Offline");
		}
	}

	public String processOnlineList(final String name, final Player trigger) {
		List<String> pub = new ArrayList<String>();
		pub.add("pub");
		ConfigurationNode node = this.list.getNode("lists." + name);
		if (node != null) {
			boolean online = node.getBoolean("online", true);
			boolean formatted = node.getBoolean("formatted", false);
			List<String> groups = node.getStringList("players.groups", pub);
			List<String> users = node.getStringList("players.users", null);
			List<String> permissions = node.getStringList("players.permissions", null);
			List<String> worlds = node.getStringList("players.worlds", null);
			String format = node.getString("format", "%nm");
			String separator = node.getString("separator", ", ");
			PlayerList playerList = new PlayerList(plugin, online, formatted, groups, users, permissions, worlds, format, separator, trigger);
			return playerList.getList();
		} else {
			return null;
		}
	}

	public String processLine(String str, BukkitVariables variables) {
		return processColors(this.methodParser.parseLine(str, variables));
	}

	public long getLastLogin(String name) {
		return this.store.getLastLogin(name);
	}

	public boolean isLocal(Player p) {
		return store.isLocal(p);
	}

	public Set<Entry> getEntries(Player trigger, String key, String event, String type) // For receivers/triggers
	{
		final String keypath = "messages." + event + "." + key + "." + type;
		final String userpath = keypath + ".users";
		final String grouppath = keypath + ".groups";
		final String permspath = keypath + ".permissions";
		final String worldpath = keypath + ".worlds";
		//@formatter:off
		return getEntries(trigger, this.plugin, 
				this.message.getStringList(grouppath, null), 
				this.message.getStringList(userpath, null),
				this.message.getStringList(permspath, null),
				this.message.getStringList(worldpath, null));
		//@formatter:on
	}

	public static Set<Entry> getEntries(final Player trigger, final Main plugin, final List<String> groups, final List<String> users, final List<String> permissions, final List<String> worlds) {
		Set<Entry> entries = new HashSet<Entry>();
		for (String group : groups) {
			boolean positive = DefaultEntry.isPositive(group);
			String unsignedGroup = DefaultEntry.getUnsignedText(group);
			if (unsignedGroup.equalsIgnoreCase("pub")) {
				entries.add(new Pub(positive ? null : trigger));
			} else if (unsignedGroup.equalsIgnoreCase("op")) {
				entries.add(new Op(positive));
			} else if (unsignedGroup.equalsIgnoreCase("pri")) {
				if (trigger != null) {
					entries.add(new Pri(positive, trigger));
				}
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
	private void preProcessMessage(final BukkitVariables variables) {
		String[] messages;
		if (variables instanceof CommandVariables) {
			messages = new String[] { ((CommandVariables) variables).command };
		} else {
			List<String> keyList = message.getKeys("messages." + variables.name);
			if (keyList == null) {
				messages = EMPTY_STRING_ARRAY;
			} else {
				messages = keyList.toArray(EMPTY_STRING_ARRAY);
			}
		}
		for (String key : messages) {
			if (!(variables instanceof DeathVariables) || !((DeathVariables) variables).key.equals(key)) {
				break;
			}
			final Player trueTrigger = getTrueTrigger(variables);
			final Set<Entry> triggers = getEntries(trueTrigger, key, variables.name, "triggers");
			if (matchEntries(trueTrigger, triggers)) {
				finishMessage(key, variables);
			}
		}
	}

	public static Player getTrueTrigger(final BukkitVariables variables) {
		final Player player;
		if (variables instanceof CommandVariables) {
			player = ((CommandVariables) variables).trigger;
		} else {
			player = null;
		}
		if (player == null) {
			return Bukkit.getPlayerExact(variables.offlinePlayer.getName());
		} else {
			return player;
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

	public static String toCapitalCase(final String string, final boolean restToLowercase) {
		final char[] chars = string.toCharArray();
		boolean found = false;
		for (int i = 0; i < chars.length; i++) {
			if (Character.isLetter(chars[i]) && !found) {
				chars[i] = Character.toUpperCase(chars[i]);
				found = true;
			} else if (restToLowercase) {
				chars[i] = Character.toLowerCase(chars[i]);
			}
		}
		return new String(chars);
	}

	public void finishMessage(String key, BukkitVariables variables) // Final touches - delay and cooldown
	{
		String[] lines = this.getLines(variables.name, key);
		if (lines.length == 0) {
			this.logger.info("Empty message named '" + key + "' (Event: '" + variables.name + "') found.");
		} else {
			final String keypath = "messages." + variables.name + "." + key;
			int cd = message.getInt(keypath + ".cooldown", 0) * 1000;
			int dl = message.getInt(keypath + ".delay", 0);

			Player[] players = this.plugin.getServer().getOnlinePlayers();
			List<Player> cooledDown = new ArrayList<Player>(players.length);
			CooldownTask task = null;
			final Player truetrigger = getTrueTrigger(variables);
			Set<Entry> receivers = getEntries(truetrigger, key, variables.name, "receivers");

			// Since the only number lower than 1000 that is possible for the
			// cooldown is 0, we make sure it isn't for the same reason as the delay.
			if (cd > 0) {
				List<String> cdstrs = new ArrayList<String>(players.length);
				for (Player player : players) {
					if (this.cooldown.isCooledDown(player, key, variables.name) && matchEntries(player, receivers)) {
						cooledDown.add(player);
						cdstrs.add(Cooldown.createKey(player, key, variables.name));
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
					new Timer().schedule(new Delay(this, lines, cooledDown, task, variables), dl);
				} else {
					sendMessage(cooledDown, lines, task, variables);
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
	public void sendMessage(Collection<Player> possibleReceivers, String[] lines, CooldownTask task, BukkitVariables variables)
	{
		ArrayList<String> processedLines = new ArrayList<String>(lines.length);
		for (int i = 0; i < lines.length; i++) {
			String processedLine = processLine(lines[i], variables);
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

	public void onPlayerJoin(PlayerJoinEvent eventObject) {
		final Player p = eventObject.getPlayer();
		final BukkitVariables variables = existingPlayer(p.getName()) ? BukkitVariables.createFirstLogin(p) : BukkitVariables.createLogin(p);
		load(variables.name);
		preProcessMessage(variables);

		if (config.getBoolean("clearjoinmsg", true)) {
			eventObject.setJoinMessage(null);
		}
	}

	public void onPlayerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (!this.kicked.remove(p.getName())) {
			final BukkitVariables variables = BukkitVariables.createQuit(p);
			load(variables.name);
			preProcessMessage(variables);

			if (config.getBoolean("clearquitmsg", true)) {
				event.setQuitMessage(null);
			}
		}
	}

	public void onPlayerKick(PlayerKickEvent event) {
		Player p = event.getPlayer();
		KickVariables kick = new KickVariables(event.getReason(), p);
		load(kick.name);
		this.kicked.add(p.getName());

		preProcessMessage(kick);

		if (config.getBoolean("clearkickmsg", true)) {
			event.setLeaveMessage(null);
		}
	}

	public void onIntervalCompletion(String key) {
		load("interval");
		finishMessage(key, BukkitVariables.createInterval(null));
	}
	
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		load(CommandVariables.NAME);
		Player sender = event.getPlayer();
		String[] cmdargs = event.getMessage().substring(1).split(" ");
		List<String> commands = message.getKeys("messages.command");

		final String command = cmdargs[0];
		if (commands != null) {
			for (String key : commands) {
				if (key.equalsIgnoreCase(cmdargs[0])) {
					event.setCancelled(true);
					if (cmdargs.length == 1) {
						preProcessMessage(new CommandVariables(command, null, sender));
					} else {
						OfflinePlayer target = plugin.getServer().getOfflinePlayer(cmdargs[1]);
						List<String> msgargs = message.getStringList("messages.command." + key + ".args", null);
						if (msgargs.contains("player")) {
							if (existingPlayer(target.getName())) {
								preProcessMessage(new CommandVariables(command, sender, target));
							} else {
								String noplayerfound = config.getString("noplayerfound", "&cPlayer \"%target()\" does not exist!");
								sender.sendMessage(processLine(noplayerfound, new PlayerNotFoundVariables(cmdargs[1], sender)));
							}
						} else {
							preProcessMessage(new CommandVariables(command, null, sender));
						}
					}
				}
			}
		}
	}
	
	public void onPlayerDeath(PlayerDeathEvent event) {
		load(DeathVariables.NAME);
		Map<String, List<String>> keyCauses = new HashMap<String, List<String>>();
		Player p = (Player) event.getEntity();
		DeathHandler handler = new DeathHandler(p, table);
		List<String> keys = message.getKeys("messages." + DeathVariables.NAME);
		
		if (keys != null) {
			for (String key : keys) {
				List<String> triggerCauses = message.getStringList("messages." + DeathVariables.NAME + "." + key + ".causes", null);
				Set<Cause> possibleCauses = handler.getCauses();
				if (triggerCauses != null) {
					keyCauses.put(key, triggerCauses);
				}
				for (String triggerCause : keyCauses.get(key)) {
					if (DeathHandler.matchCauses(toCapitalCase(triggerCause), possibleCauses)) {
						//TODO: Define trigger!
						final String item = handler.isKillerPlayer() ? handler.getItem(plugin.getServer().getPlayerExact(handler.getKiller()).getItemInHand()) : "?";
						DeathVariables variables = new DeathVariables(item, handler.getKiller(), key, p);
						preProcessMessage(variables);
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