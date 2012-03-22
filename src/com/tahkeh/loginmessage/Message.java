package com.tahkeh.loginmessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
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

import javax.script.ScriptEngineManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.tahkeh.loginmessage.handlers.AFKHandler;
import com.tahkeh.loginmessage.handlers.DeathHandler;
import com.tahkeh.loginmessage.handlers.PlayerDataHandler;
import com.tahkeh.loginmessage.matcher.DefaultMatcher;
import com.tahkeh.loginmessage.matcher.DefaultMatcher.SignedTextData;
import com.tahkeh.loginmessage.matcher.causes.Cause;
import com.tahkeh.loginmessage.matcher.causes.EntityCause;
import com.tahkeh.loginmessage.matcher.causes.EnumCause;
import com.tahkeh.loginmessage.matcher.causes.ProjectileCause;
import com.tahkeh.loginmessage.matcher.entries.Entry;
import com.tahkeh.loginmessage.matcher.entries.Group;
import com.tahkeh.loginmessage.matcher.entries.Op;
import com.tahkeh.loginmessage.matcher.entries.Permission;
import com.tahkeh.loginmessage.matcher.entries.Pri;
import com.tahkeh.loginmessage.matcher.entries.Pub;
import com.tahkeh.loginmessage.matcher.entries.User;
import com.tahkeh.loginmessage.store.MaterialTable;
import com.tahkeh.loginmessage.store.PropertiesFile;
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
import com.tahkeh.loginmessage.methods.variables.bukkit.FirstLoginVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.IntervalVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.KeyVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.KickVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.LoginVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerNotFoundVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.QuitVariables;
import com.tahkeh.loginmessage.timers.Cooldown;
import com.tahkeh.loginmessage.timers.Delay;
import com.tahkeh.loginmessage.timers.Cooldown.CooldownTask;
import com.tahkeh.loginmessage.timers.Interval;

import de.xzise.MinecraftUtil;
import de.xzise.MinecraftUtil.ChanceElement;
import de.xzise.MinecraftUtil.DefaultChanceElement;
import de.xzise.XLogger;
import de.xzise.bukkit.util.MemorySectionFromMap;
import de.xzise.wrappers.permissions.BufferPermission;

public class Message
{
	private final static String[] EMPTY_STRING_ARRAY = new String[0];
	private final static BufferPermission<String> PREFIX_PERMISSION = BufferPermission.create("prefix", (String) null);
	private final static BufferPermission<String> SUFFIX_PERMISSION = BufferPermission.create("suffix", (String) null);

	private final Main plugin;

	private final FileConfigurationPair<YamlConfiguration> config;
	private final FileConfigurationPair<YamlConfiguration> message;
	private final FileConfigurationPair<YamlConfiguration> list;
	private final XLogger logger;
	private final MaterialTable table;
	private final MethodParser<BukkitVariables> methodParser;
	private final PlayerDataHandler data;
	private final Set<String> kicked = new HashSet<String>();
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
		this.methodParser = new MethodParser<BukkitVariables>(logger, "%");
		addGeoipFail();
		File geoip = new File(plugin.getDataFolder(), "GeoLiteCity.dat");
		PropertiesFile store = new PropertiesFile(new File(plugin.getDataFolder(), "store.txt"), logger);
		this.data = new PlayerDataHandler(store, geoip);
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

	private static <T> List<T> getNonNullList(final List<T> rawList) {
		if (rawList == null) {
			return new ArrayList<T>(0);
		} else {
			return rawList;
		}
	}

	public void load(final boolean forceLoad, final boolean schedule) {
		if(config.fileConfiguration.getBoolean("autoload", true) || forceLoad) {
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

			YamlConfiguration configuration = FileConfigurationPair.load(new File(this.plugin.getDataFolder(), "methods.yml"), new YamlConfiguration(), "methods", this.logger);
			Map<String, MemorySectionFromMap> scriptNodes = MemorySectionFromMap.getNodes(configuration, "script");
			int scriptEnabled = 0;
			if (scriptNodes != null) {
				ScriptEngineManager engineManager = new ScriptEngineManager();
				for (Map.Entry<String, MemorySectionFromMap> scriptNode : scriptNodes.entrySet()) {
					String name = scriptNode.getKey();
					MemorySectionFromMap values = scriptNode.getValue();

					String method = values.getString("method", "call");
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
						ScriptMethod methodObj = ScriptMethod.create(engine, method, reader, engineManager, logger);
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

			Map<String, MemorySectionFromMap> aliasNodes = MemorySectionFromMap.getNodes(configuration, "alias");
			int aliasEnabled = 0;
			if (aliasNodes != null) {
				for (Map.Entry<String, MemorySectionFromMap> aliasNode : aliasNodes.entrySet()) {
					String name = aliasNode.getKey();
					MemorySectionFromMap values = aliasNode.getValue();
					String calls = values.getString("call");
					int paramCount = values.getInt("parameters", 0);
					if (calls != null) {
						AliasMethod.create(calls, paramCount, name, this.methodParser).register();
						aliasEnabled++;
					} else {
						this.logger.warning("No call definition for alias method '" + name + "'!");
					}
				}
			}

			Map<String, MemorySectionFromMap> redirectedNodes = MemorySectionFromMap.getNodes(configuration, "redirect");
			int redirectedEnabled = 0;
			if (redirectedNodes != null) {
				List<MethodParser.RedirectedElement> redirectedElements = new ArrayList<MethodParser.RedirectedElement>(redirectedNodes.size() * 2);
				for (Map.Entry<String, MemorySectionFromMap> redirectedNode : redirectedNodes.entrySet()) {
					String name = redirectedNode.getKey();
					MemorySectionFromMap values = redirectedNode.getValue();
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
		addGeoipFail();
		if (schedule) {
			scheduleIntervals();
		}
	}

	private static int[] getParamCounts(final ConfigurationSection node, final String name, final int... counts) {
		final List<Integer> parameterCounts = getNonNullList(node.getIntegerList(name));
		if (parameterCounts.size() == 0) {
			return counts;
		} else {
			final int[] paramCountArray = new int[parameterCounts.size()];
			int idx = 0;
			for (Integer integer : parameterCounts) {
				paramCountArray[idx++] = integer;
			}
			return paramCountArray;
		}
	}

	public SimpleDateFormat getDateFormat(final String name, final String defaultFormat) {
		return new SimpleDateFormat(this.config.fileConfiguration.getString("format." + name, defaultFormat));
	}

	public void scheduleIntervals() {
		Set<String> keys = getKeys(this.message.fileConfiguration, "messages.interval");
		if(keys != null) {
			for(String key : keys) {
				if(!running.contains(key)) {
					int interval = this.message.fileConfiguration.getInt("messages.interval." + key + ".interval", 300);
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
		return string.replaceAll("(&([a-fkA-FK0-9]))", ChatColor.COLOR_CHAR + "$2");
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
	 * Get the first loaded, or default world.
	 * @return the default world
	 */
	public World getDefaultWorld() {
		  return plugin.getServer().getWorlds().get(0);
		}

	public String getLocation(String type, String p) {
		return data.lookupGeoIP(PlayerDataHandler.getPlayer(p), type, geoipFail);
	}

	public String getTime(Long rawtime) {
		int modTime = (int) (rawtime % 24000);

		if (modTime == 24000 || modTime <= 11999) {
			return config.fileConfiguration.getString("day");
		} else if (modTime == 12000 || modTime <= 12999) {
			return config.fileConfiguration.getString("sunset");
		} else if (modTime == 13000 || modTime <= 22999) {
			return config.fileConfiguration.getString("night");
		} else if (modTime == 23000 || modTime <= 23999) {
			return config.fileConfiguration.getString("sunrise");
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
		return this.config.fileConfiguration.getString("mode." + mode.getValue(), getDefaultGameModeText(mode));
	}

	public static String getPrefix(String group, String world) {
		return Main.getPermissions().getString(world, group, PREFIX_PERMISSION);
	}

	public static String getSuffix(String group, String world) {
		return Main.getPermissions().getString(world, group, SUFFIX_PERMISSION);
	}

	public String booleanToName(boolean bool) {
		return bool ? config.fileConfiguration.getString("istrue", "&2Yes") : config.fileConfiguration.getString("isfalse", "&4No");
	}

	public String getStatus(OfflinePlayer p) {
		if (p.isOnline()) {
			AFKHandler afkhandler = new AFKHandler(plugin);
			if (afkhandler.isActive() && afkhandler.isAFK(plugin.getServer().getPlayerExact(p.getName()))) {
				return config.fileConfiguration.getString("status.afk", "&6AFK");
			} else {
				return config.fileConfiguration.getString("status.online", "&2Online");
			}
		} else {
			return config.fileConfiguration.getString("status.offline", "&7Offline");
		}
	}

	public String processOnlineList(final String name, final Player trigger) {
		ConfigurationSection node = this.list.fileConfiguration.getConfigurationSection("lists." + name);
		if (node != null) {
			boolean online = node.getBoolean("online", true);
			boolean formatted = node.getBoolean("formatted", false);
			List<String> groups = node.getStringList("players.groups");
			if (groups == null) {
				groups = Arrays.asList("pub");
			}
			List<String> users = getNonNullList(node.getStringList("players.users"));
			List<String> permissions = getNonNullList(node.getStringList("players.permissions"));
			List<String> worlds = getNonNullList(node.getStringList("players.worlds"));
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
		return this.data.getLong(this.plugin.getServer().getPlayerExact(name), "laston", PlayerDataHandler.getTime());
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
				getNonNullList(this.message.fileConfiguration.getStringList(grouppath)), 
				getNonNullList(this.message.fileConfiguration.getStringList(userpath)),
				getNonNullList(this.message.fileConfiguration.getStringList(permspath)),
				getNonNullList(this.message.fileConfiguration.getStringList(worldpath)));
		//@formatter:on
	}

	public static Set<Entry> getEntries(final Player trigger, final Main plugin, final List<String> groups, final List<String> users, final List<String> permissions, final List<String> worlds) {
		Set<Entry> entries = new HashSet<Entry>();
		for (String group : groups) {
			final SignedTextData signedTextData = new SignedTextData(group);
			if (signedTextData.unsignedText.equalsIgnoreCase("pub")) {
				entries.add(new Pub(signedTextData.positive ? null : trigger));
			} else if (signedTextData.unsignedText.equalsIgnoreCase("op")) {
				entries.add(new Op(signedTextData.positive));
			} else if (signedTextData.unsignedText.equalsIgnoreCase("pri")) {
				if (trigger != null) {
					entries.add(new Pri(signedTextData.positive, trigger));
				}
			} else {
				entries.add(new Group(signedTextData, Main.getPermissions(), plugin));
			}
		}

		for (String user : users) {
			entries.add(new User(user));
		}

		for (String perm : permissions) {
			entries.add(new Permission(perm, Main.getPermissions(), plugin));
		}

		for (String world : worlds) {
			entries.add(new com.tahkeh.loginmessage.matcher.entries.World(world, plugin));
		}
		return entries;
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
	private void preProcessMessage(final BukkitVariables variables) {
		final String[] messageKeys;
		if (variables instanceof KeyVariables) {
			messageKeys = ((KeyVariables) variables).getKeys();
		} else {
			Set<String> keyList = getKeys(message.fileConfiguration, "messages." + variables.name);
			if (keyList == null) {
				messageKeys = EMPTY_STRING_ARRAY;
			} else {
				messageKeys = keyList.toArray(EMPTY_STRING_ARRAY);
			}
		}
		for (String key : messageKeys) {
			final Player trueTrigger = getTrueTrigger(variables);
			final Set<Entry> triggers = getEntries(trueTrigger, key, variables.name, "triggers");
			if (DefaultMatcher.match(trueTrigger, triggers)) {
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
		if (player == null && variables instanceof PlayerVariables) {
			return Bukkit.getPlayerExact(((PlayerVariables) variables).offlinePlayer.getName());
		} else {
			return player;
		}
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
		List<MemorySectionFromMap> linesList = MemorySectionFromMap.getSectionList(this.message.fileConfiguration, "messages." + event + "." + name + ".messages");
		String[] lines = EMPTY_STRING_ARRAY;
		if (MinecraftUtil.isSet(linesList)) {
			// if everything has a order it is ordered
			final int idx;
			boolean ordered = true;
			Integer buffer = null;
			for (MemorySectionFromMap memorySectionFromMap : linesList) {
				if (memorySectionFromMap.isInt("order")) {
					final int order = memorySectionFromMap.getInt("order");
					if (buffer == null || buffer < order) {
						buffer = order;
					}
				} else {
					ordered = false;
					break;
				}
			}
			if (buffer == null) {
				ordered = false;
				idx = 0;
			} else if (this.cycle.get(name) == null) {
				idx = buffer;
			} else {
				idx = this.cycle.get(name);
			}
			MemorySectionFromMap selected = null;
			if (ordered) {
				int next = idx;
				boolean found = false;
				for (MemorySectionFromMap memorySectionFromMap : linesList) {
					if (memorySectionFromMap.isInt("order")) {
						final int order = memorySectionFromMap.getInt("order");
						if (order == idx) {
							selected = memorySectionFromMap;
							found = true;
						} else if (selected == null || (!found && selected.getInt("order") > order)) {
							selected = memorySectionFromMap;
						}
						if (order > idx && order < next) {
							next = order;
						}
					} else {
						ordered = false;
						break;
					}
				}
				this.cycle.put(name, next);
			} else {
				final double defChance = 1.0 / linesList.size();
				List<ChanceElement<MemorySectionFromMap>> chances = new ArrayList<ChanceElement<MemorySectionFromMap>>(linesList.size());
				for (MemorySectionFromMap memorySectionFromMap : linesList) {
					chances.add(new DefaultChanceElement<MemorySectionFromMap>(memorySectionFromMap.getDouble("chance", defChance), memorySectionFromMap));
				}
				selected = MinecraftUtil.getRandomFromChances(chances);
			}
			if (selected != null) {
				lines = getStringList(selected.get("message"), EMPTY_STRING_ARRAY);
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
			int cd = message.fileConfiguration.getInt(keypath + ".cooldown", 0) * 1000;
			int dl = message.fileConfiguration.getInt(keypath + ".delay", 0);

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
					if (this.cooldown.isCooledDown(player, key, variables.name) && DefaultMatcher.match(player, receivers)) {
						cooledDown.add(player);
						cdstrs.add(Cooldown.createKey(player, key, variables.name));
					}
				}
				task = this.cooldown.createTask(cdstrs, this.cooldown, cd);
			} else {
				for (Player player : players) {
					if (DefaultMatcher.match(player, receivers)) {
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

	public String getIP(final Player player) {
		return this.data.getIP(player);
	}

	public void onPlayerJoin(PlayerJoinEvent eventObject) {
		final Player p = eventObject.getPlayer();
		final BukkitVariables variables = p.hasPlayedBefore() ? new LoginVariables(p) : new FirstLoginVariables(p);
		load(false, true);
		preProcessMessage(variables);

		if (config.fileConfiguration.getBoolean("clearjoinmsg", true)) {
			eventObject.setJoinMessage(null);
		}
	}

	public void onPlayerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (!this.kicked.remove(p.getName())) {
			final BukkitVariables variables = new QuitVariables(p);
			load(false, true);
			preProcessMessage(variables);

			if (config.fileConfiguration.getBoolean("clearquitmsg", true)) {
				event.setQuitMessage(null);
			}
		}
	}

	public void onPlayerKick(PlayerKickEvent event) {
		Player p = event.getPlayer();
		KickVariables kick = new KickVariables(event.getReason(), p);
		load(false, true);
		this.kicked.add(p.getName());

		preProcessMessage(kick);

		if (config.fileConfiguration.getBoolean("clearkickmsg", true)) {
			event.setLeaveMessage(null);
		}
	}

	public void onIntervalCompletion(String key) {
		load(false, false);
		finishMessage(key, new IntervalVariables());
	}

	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		load(false, true);
		Player sender = event.getPlayer();
		String[] cmdargs = event.getMessage().substring(1).split(" ");
		final String path = "messages." + CommandVariables.NAME;
		Set<String> commands = getKeys(message.fileConfiguration, path);

		final String command = cmdargs[0];
		if (commands != null) {
			for (String key : commands) {
				
				if (key.equalsIgnoreCase(cmdargs[0])) {
					event.setCancelled(true);
					if (cmdargs.length == 1) {
						preProcessMessage(new CommandVariables(command, null, sender));
					} else {
						OfflinePlayer target = plugin.getServer().getOfflinePlayer(cmdargs[1]);
						List<String> msgargs = getNonNullList(message.fileConfiguration.getStringList(path + "." + key + ".args"));
						if (msgargs.contains("player")) {
							if (target.hasPlayedBefore()) {
								preProcessMessage(new CommandVariables(command, sender, target));
							} else {
								final String npf = config.fileConfiguration.getString("noplayerfound", "&cPlayer \"%nm\" does not exist!");
								sender.sendMessage(processLine(npf, new PlayerNotFoundVariables(cmdargs[1], sender)));
							}
						} else {
							preProcessMessage(new CommandVariables(command, null, sender));
						}
					}
				}
			}
		}
	}

	private static List<Cause> getCauses(final YamlConfiguration message, final String path, final XLogger logger) {
		final List<Cause> causes = new ArrayList<Cause>();
		final String causesPath = path + ".causes";
		List<MemorySectionFromMap> projectiles = MemorySectionFromMap.getSectionList(message, causesPath + ".projectiles");
		for (MemorySectionFromMap projectile : projectiles) {
			if (projectile.isString("shooter") && projectile.isString("damager")) {
				causes.add(new ProjectileCause(projectile.getString("shooter"), projectile.getString("damager"), projectile.getBoolean("positive", true), logger));
			}
		}
		for (String entity : getNonNullList(message.getStringList(causesPath + ".entity"))) {
			if (entity != null) {
				causes.add(new EntityCause(entity, logger));
			}
		}
		for (String other : getNonNullList(message.getStringList(causesPath + ".other"))) {
			if (other != null) {
				final EnumCause cause = EnumCause.create(other);
				if (cause != null) {
					causes.add(cause);
				}
			}
		}
		return causes;
	}

	private static String getEntityName(final Entity entity) {
		if (entity instanceof Player) {
			return ((Player) entity).getName();
		} else {
			final String className = entity.getClass().getSimpleName();
			if (className.startsWith("Craft")) {
				return className.substring(5);
			} else {
				return className;
			}
		}
	}

	//@formatter:off
	// 1st "cause" generation
	/*
	 * causes:
	 *   - CraftSkeleton
	 *   - CraftGhast
	 *   - CraftZombie
	 *   - Lava
	 */
	// 2nd "cause" generation
	/*
	 * # B: for org.bukkit.entity
	 * # C: for org.bukkit.craftbukkit.entity
	 * gen: 2 # to show, that the causes are working with the 2nd generation
	 * causes:
	 *   projectiles:
	 *     - shooter: B:Skeleton
	 *       damager: B:Projectile
	 *     - shooter: B:Ghast
	 *       damager: B:Fireball
	 *     - shooter: *
	 *       damager: B:SmallFireball
	 *       positive: false
	 *   entity:
	 *     - B:Zombie
	 *   other:
	 *     - Lava
	 */
	//@formatter:on
	public void onPlayerDeath(PlayerDeathEvent event) {
		load(false, true);
		Map<String, List<String>> keyCauses = new HashMap<String, List<String>>();
		Player p = (Player) event.getEntity();
		DeathHandler handler = new DeathHandler(p, table);
		final String path = "messages." + DeathVariables.NAME;
		final Set<String> keys = getKeys(message.fileConfiguration, path);
		final Set<String> matchedKeys = new HashSet<String>(keys.size());
		
		if (keys != null) {
			for (String key : keys) {
				final int generation = message.fileConfiguration.getInt(path + "." + key + ".gen", 0);
				if (generation == 2) {
					final List<Cause> causes = getCauses(this.message.fileConfiguration, path + "." + key, this.logger);
					if (DefaultMatcher.match(p.getLastDamageCause(), causes)) {
						matchedKeys.add(key);
					}
				} else {
					// Old way to handle this
					List<String> triggerCauses = getNonNullList(message.fileConfiguration.getStringList(path + "." + key + ".causes"));
					Set<com.tahkeh.loginmessage.entries.causes.Cause> possibleCauses = handler.getCauses();
					if (triggerCauses != null) {
						keyCauses.put(key, triggerCauses);
					}
					if (DeathHandler.matchCauses(keyCauses.get(key), possibleCauses)) {
						final String item = handler.isKillerPlayer() ? handler.getItem(plugin.getServer().getPlayerExact(handler.getKiller()).getItemInHand()) : "?";
						DeathVariables variables = new DeathVariables(item, handler.getKiller(), new String[] { key }, p);
						preProcessMessage(variables);
					}
				}
			}
		}

		if (matchedKeys.size() > 0) {
			final EntityDamageEvent damageEvent = p.getLastDamageCause();
			final String killer;
			String item = null;
			if (damageEvent instanceof EntityDamageByEntityEvent) {
				final Entity damager = ((EntityDamageByEntityEvent) damageEvent).getDamager();
				final Entity killerObject;
				if (damager instanceof Projectile) {
					killerObject = ((Projectile) damager).getShooter();
				} else {
					killerObject = damager;
				}
				killer = getEntityName(killerObject);
				if (killerObject instanceof Player) {
					item = this.table.getMaterialName(((Player) killerObject).getItemInHand().getData());
				}
			} else if (damageEvent instanceof EntityDamageByBlockEvent) {
				final Block damager = ((EntityDamageByBlockEvent) damageEvent).getDamager();
				if (damager != null) {
					killer = this.table.getMaterialName(damager.getTypeId(), damager.getData());
				} else {
					killer = this.table.getMaterialName(Material.AIR);
				}
			} else {
				killer = damageEvent.getCause().name();
			}
			DeathVariables variables = new DeathVariables(item, killer, matchedKeys.toArray(new String[0]), p);
			preProcessMessage(variables);
		}
		
		if (config.fileConfiguration.getBoolean("cleardeathmsg", true)) {
			event.setDeathMessage(null);
		}
	}
}
