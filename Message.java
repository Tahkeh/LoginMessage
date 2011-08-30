package com.tahkeh.loginmessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

import com.iConomy.iConomy;
import com.iConomy.system.Holdings;
import com.maxmind.geoip.*;
import com.nijiko.permissions.PermissionHandler;

import com.tahkeh.loginmessage.entries.DefaultEntry;
import com.tahkeh.loginmessage.entries.Entry;
import com.tahkeh.loginmessage.entries.Group;
import com.tahkeh.loginmessage.entries.Op;
import com.tahkeh.loginmessage.entries.Permission;
import com.tahkeh.loginmessage.entries.Pri;
import com.tahkeh.loginmessage.entries.Pub;
import com.tahkeh.loginmessage.entries.User;
import com.tahkeh.loginmessage.perm.PermissionsChecker;
import com.tahkeh.loginmessage.timers.Cooldown;
import com.tahkeh.loginmessage.timers.Delay;
import com.tahkeh.loginmessage.timers.Cooldown.CooldownTask;

public class Message extends PlayerListener {
	public final static char SECTION_SIGN = '\u00A7';
	private final Main plugin;
	private final Configuration config;
	private final Configuration message;
	private final Logger log;
	public static Properties prop = new Properties();
	String separator = "%&%&";

	private final Cooldown cooldown;

	public Message(Main instance, Configuration config, Configuration message, Logger log) {
		this.plugin = instance;
		this.config = config;
		this.message = message;
		this.log = log;
		this.cooldown = new Cooldown();
	}
	
	public void load() {
		File store = new File(plugin.getDataFolder(), "store.txt");
		boolean initial = false;
		if (!store.exists()) {
			initialLoad(store);
			initial = true;
		}
		//populateProperties("", store);
		if (initial) {
			log.info("[LoginMessage] Initial load complete!");
		}
	}
	
	public void initialLoad(File store) {
		log.info("[LoginMessage] Beginning initial load...");
		try {
			store.createNewFile();
			FileOutputStream o = new FileOutputStream(store);
			prop.store(o, "LoginMessage property store file - don't edit this unless you know what you're doing!");
			o.flush();
			o.close();
		} catch (IOException e) {
			log.info("[LoginMessage] Unable to create store.txt. Aborting initial load.");
			return;
		}
	}
	
	public void populateProperties(String event, File store) { // Method currently buggy, do not use.
		config.load();
		File geoip = new File(plugin.getDataFolder(), "GeoLiteCity.dat");
		try {
			FileOutputStream o = new FileOutputStream(store);
			if(plugin.getServer().getOnlinePlayers().length > 0) {
				for(Player p : plugin.getServer().getOnlinePlayers()) {
					if(geoip.exists()) {
							String ip = isLocal(p) ? Main.getExternalIp().getHostAddress() : p.getAddress().getAddress().getHostAddress();
							LookupService ls = new LookupService(geoip);
							Location loc = ls.getLocation(ip);
							if(prop.getProperty(p.getName() + ".city", "").isEmpty() && loc.city != null) {
								prop.put(p.getName() + ".city", loc.city);
							}
							if(prop.getProperty(p.getName() + ".ccode", "").isEmpty() && loc.countryCode != null) {
								prop.put(p.getName() + ".ccode", loc.countryCode);
							}
							if(prop.getProperty(p.getName() + ".cname", "").isEmpty() && loc.countryName != null) {
								prop.put(p.getName() + ".cname", loc.countryName);
							}
							if(prop.getProperty(p.getName() + ".zip", "").isEmpty() && loc.postalCode != null) {
								prop.put(p.getName() + ".zip", loc.postalCode);
							}
							if(prop.getProperty(p.getName() + ".rcode", "").isEmpty() && loc.region != null) {
								prop.put(p.getName() + ".rcode", loc.region);
							}
							if(prop.getProperty(p.getName() + ".rname", "").isEmpty() && loc.countryCode != null && loc.region != null) {
								String ccode = prop.getProperty(p.getName() + ".ccode", loc.countryCode);
								String rcode = prop.getProperty(p.getName() + ".rcode", loc.region);
								prop.put(p.getName() + ".rname", regionName.regionNameByCode(ccode, rcode));
							}
							ls.close();
					}
				}
			}
			prop.store(o, "LoginMessage property store file - don't edit this unless you know what you're doing!");
			o.close();
		} catch (IOException e) {
		}
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
	
	public String getLocation(String type, Player p, String event) {
		load();
		config.load();
		File store = new File(plugin.getDataFolder(), "store.txt");
		String location = "";
		try {
			FileInputStream in = new FileInputStream(store);
			prop.load(in);
			location = prop.getProperty(p.getName() + "." + type, process(config.getProperty(type + "fail").toString(), p, event));
		} catch (IOException e) {
		}
		return location;
	}
	
	public String getTime(Long rawtime, boolean caps) {
		String Day = config.getString("day");
		String day = Day.toLowerCase();

		String Dusk = config.getString("sunset");
		String dusk = Dusk.toLowerCase();

		String Dawn = config.getString("sunrise");
		String dawn = Dawn.toLowerCase();

		String Night = config.getString("night");
		String night = Night.toLowerCase();

		if (caps == false) {
			if (rawtime == 24000 || rawtime <= 11999) {
				return day;
			} else if (rawtime == 12000 || rawtime <= 12999) {
				return dusk;
			} else if (rawtime == 13000 || rawtime <= 22999) {
				return night;
			} else if (rawtime == 23000 || rawtime <= 23999) {
				return dawn;
			}
		} else if (rawtime == 24000 || rawtime <= 11999) {
			return Day;
		} else if (rawtime == 12000 || rawtime <= 12999) {
			return Dusk;
		} else if (rawtime == 13000 || rawtime <= 22999) {
			return Night;
		} else if (rawtime == 23000 || rawtime <= 23999) {
			return Dawn;
		}
		return "";
	}

	public String olProcess(String str, Player player, String event) {
		int on = 0;
		String list = "";
		String s = str;

		while (s.indexOf(separator) >= 0) {
			s = s.substring(s.indexOf(separator) + 1);
		}
		Player[] online = plugin.getServer().getOnlinePlayers();
		int length = online.length - 1;
		List<Player> all_list = new ArrayList<Player>();
		for (Player all : online) {
			while (!all_list.contains(all)) {
				all_list.add(all);
				// Create a list of all players that we can remove stuff from
			}
			if (isLeaveEvent(event)) {
				all_list.remove(player);
				length = length - 1;
			}
		}

		if (s.substring(separator.length() - 1, s.indexOf(":")).length() == 1
				|| s.substring(separator.length() - 1, s.indexOf(":")).length() == 0) {
			PermissionHandler handler = Main.getPermissions();
			// Credit to mathmaniac43 for awesome string trimming with substring()
			String a = s.substring(separator.length() - 1, s.indexOf(":"));
			s = s.substring(s.indexOf(":") + 1);

			String b = s.substring(0, s.indexOf(":"));
			s = s.substring(s.indexOf(":") + 1);

			String c = s.substring(0, 2);
			boolean sf1 = false;
			boolean pr1 = false;

			boolean sf2 = false;
			boolean pr2 = false;

			if (b.equalsIgnoreCase("sf")) {
				sf1 = true;
				pr1 = false;
			} else if (b.equalsIgnoreCase("pr")) {
				sf1 = false;
				pr1 = true;
			}
			if (c.equalsIgnoreCase("sf")) {
				sf2 = true;
				pr2 = false;
			} else if (c.equalsIgnoreCase("pr")) {
				sf2 = false;
				pr2 = true;
			}
			for (Player current : all_list) {
				String b1 = "";
				String c1 = "";
				if (plugin.PermissionsEnabled()) {
					b1 = (sf1 && !pr1 && plugin.PermissionsEnabled() ? handler.getGroupSuffix(current.getWorld().getName(), handler.getGroup(current.getWorld().getName(), current.getName())) : handler.getGroupPrefix(current.getWorld().getName(), handler.getGroup(current.getWorld().getName(), current.getName())));
					c1 = (sf2 && !pr2 && plugin.PermissionsEnabled() ? handler.getGroupSuffix(current.getWorld().getName(), handler.getGroup(current.getWorld().getName(), current.getName())) : handler.getGroupPrefix(current.getWorld().getName(), handler.getGroup(current.getWorld().getName(), current.getName())));
				}
				if (!sf1 && !pr1) {
					b1 = b; // If we're using pr/sf, then the string will be b1, which is defined earlier
				}
				if (!sf2 && !pr2) {
					c1 = c; // Same deal as before
				}
				b1 = b1.replaceAll("(&([a-z0-9]))", SECTION_SIGN + "$2"); // My good buddy replace-ampersands-with-section-signs regex
				c1 = c1.replaceAll("(&([a-z0-9]))", SECTION_SIGN + "$2");
				if (a.equals("d")) {
					list = list + (on >= length ? b1 + current.getDisplayName(): new StringBuilder().append(b1 + current.getDisplayName()).append(c1 + ", ").toString());
					on++;
				} else {
					list = list + (on >= length ? b1 + current.getName() : new StringBuilder().append(b1 + current.getName()).append(c1 + ", ").toString());
					on++;
				}
			}

			String ol = "%ol" + separator + a + ":" + b + ":" + c;
			str = str.replaceAll(ol, list);
		}
		return str;
	}

	public boolean isLeaveEvent(String event) {
		return event.equals("kick") || event.equals("quit");
	}

	public String process(String str, Player player, String event) {
		config.load();
		message.load();
		Player[] online = plugin.getServer().getOnlinePlayers();
		int serverlist = online.length;
		if (isLeaveEvent(event)) {
			serverlist = serverlist - 1;
		}
		int servermax = plugin.getServer().getMaxPlayers();
		str = str.replaceAll("%dpnm", player.getDisplayName());
		str = str.replaceAll("%nm", player.getName());
		str = str.replaceAll("%size", Integer.toString(serverlist));
		str = str.replaceAll("%max", Integer.toString(servermax));
		if (str.contains("%ol" + separator)) {
			str = olProcess(str, player, event);
		} else if (str.contains("%ol")) {
			String list = "";
			int on = 0;
			List<Player> all_list = new ArrayList<Player>();
			for (Player all : online) {
				while (!all_list.contains(all)) {
					all_list.add(all);
				}
				if (isLeaveEvent(event)) {
					all_list.remove(player);
				}
			for (Player current : all_list) {
		        if (current == null) { on++;
		        } else {
		          list = list + (on >= serverlist ? current.getName() : new StringBuilder().append(current.getName()).append(", ").toString());
		          on++;
		        }
		      }
			}
		}
		if (plugin.iConomyEnabled()) {
			if (!iConomy.hasAccount(player.getName())) {
				str = str.replaceAll("%bal", "");
			}
			Holdings balance = iConomy.getAccount(player.getName()).getHoldings();
			str = str.replaceAll("%bal", balance.toString());
		}
		if (plugin.PermissionsEnabled()) {
			PermissionHandler handler = Main.getPermissions();
			String groupname = handler.getGroup(player.getWorld().getName(),
					player.getName());
			str = str.replaceAll("%group", groupname);

			if (handler.getGroupPrefix(player.getWorld().getName(), groupname) != null || handler.getGroupSuffix(player.getWorld().getName(), groupname) != null) {
				String prefix = handler.getGroupPrefix(player.getWorld().getName(), groupname);
				String suffix = handler.getGroupSuffix(player.getWorld().getName(), groupname);
				String permissionslist = "";
				int length1 = online.length - 1;
				int on1 = 0;
				for (Player current : online) {
					if (current == null) {
						on1++;
					} else {
						String prefix2 = handler.getGroupPrefix(current.getWorld().getName(), handler.getGroup(current.getWorld().getName(), current.getName()));
						String suffix2 = handler.getGroupSuffix(current.getWorld().getName(), handler.getGroup(current.getWorld().getName(), current.getName()));
						permissionslist = permissionslist + (on1 >= length1 ? prefix2 + current.getName() + suffix2 : new StringBuilder().append(prefix2).append(current.getName()).append(suffix2).append(", ").toString());
						on1++;
					}
				}
				str = str.replaceAll("%prefix", prefix);
				str = str.replaceAll("%suffix", suffix);
				str = str.replaceAll("%perol", permissionslist);
			}
		}
		String ip = player.getAddress().getAddress().getHostAddress();
		if (!isLocal(player)) {
			str = str.replaceAll("%ip", ip);
		} else {
			str = str.replaceAll("%ip", Main.getExternalIp().getHostAddress());
		}
		if(str.contains("%city")) {
			str = str.replaceAll("%city", getLocation("city", player, event));
		}
		if(str.contains("%ccode")) {
			str = str.replaceAll("%ccode", getLocation("ccode", player, event));
		}
		if(str.contains("%cname")) {
			str = str.replaceAll("%cname", getLocation("cname", player, event));
		}
		if(str.contains("%zip")) {
			str = str.replaceAll("%zip", getLocation("zip", player, event));
		}
		if(str.contains("%rcode")) {
			str = str.replaceAll("%rcode", getLocation("rcode", player, event));
		}
		if(str.contains("%rname")) {
			str = str.replaceAll("%rname", getLocation("rname", player, event));
		}
		Long rawtime = player.getWorld().getTime();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(config.getString("format"));
		str = str.replaceAll("%world", player.getWorld().getName());
		str = str.replaceAll("%rtime", rawtime.toString());
		str = str.replaceAll("%time", getTime(rawtime, false));
		str = str.replaceAll("%Time", getTime(rawtime, true));
		str = str.replaceAll("%srtime", sdf.format(cal.getTime()));
		str = str.replaceAll("(&([a-z0-9]))", SECTION_SIGN + "$2");
		str = str.replaceAll("%sp", "");
		return str;
	}

	public boolean isLocal(Player p) {
		boolean r = false;
		try {
			String localip = InetAddress.getLocalHost().getHostAddress();
			String ip = p.getAddress().getAddress().getHostAddress();
			String shortlocalip = localip.substring(0, localip.length() - 3); // The last 3 indexes
			String shortip = ip.substring(0, ip.length() - 3); // could differ, so we exclude them
			
			if(shortlocalip.contains(shortip) || shortip.contains(shortlocalip)) {
				r = true;
			}
		} catch (UnknownHostException e) {
		}
		return r;
	}
	/**
	 * Gets receivers or triggers.
	 * @param trigger
	 * 			the player triggering the event
	 * @param key
	 * 			the name of the message
	 * @param event
	 * 			the event type
	 * @param type
	 * 			the type of entries to get (receivers/triggers)
	 * @return the set of entries
	 */
	public Set<Entry> getEntries(Player trigger, String key, String event, String type)
	{
		message.load();
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
			} else if (this.plugin.PermissionsEnabled()) {
				entries.add(new Group(group, Main.getPermissions()));
			}
		}

		for (String user : message.getStringList(userpath, null)) {
			entries.add(new User(user));
		}

		PermissionsChecker checker;
		if (this.plugin.PermissionsEnabled()) {
			checker = new PermissionsChecker.PermissionsPluginChecker(
					Main.getPermissions());
		} else {
			checker = new PermissionsChecker.InternalPermissionsChecker();
		}

		for (String perm : message.getStringList(permspath, null)) {
			entries.add(new Permission(perm, checker));
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
	public void preProcessMessage(Player trigger, String event, String cmdkey) {
		message.load();
		config.load();

		String[] messages;
		if (event.equals("command")) {
			messages = new String[] { cmdkey };
		} else {
			messages = message.getKeys("messages." + event).toArray(new String[0]);
		}
		for (String key : messages) {
			Set<Entry> triggers = getEntries(trigger, key, event, "triggers");
			if (matchEntries(trigger, triggers)) {
				finishMessage(trigger, event, key);
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
		String[] lines = getStringList(message, "messages." + event + "." + name + ".message", new String[0]);
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

	/**
	 * Handle cooldown and delay variables
	 * 
	 * @param p
	 *            the trigger player
	 * @param event
	 *            the event
	 * @param key
	 *            the message name
	 */
	public void finishMessage(Player p, String event, String key) {
		message.load();
		final String keypath = "messages." + event + "." + key;
		int cd = message.getInt(keypath + ".cooldown", 0) * 1000;
		int dl = message.getInt(keypath + ".delay", 0);

		Player[] players = this.plugin.getServer().getOnlinePlayers();
		List<Player> cooledDown = new ArrayList<Player>(players.length);
		CooldownTask task = null;

		// Since the only number lower than 1000 that is possible for the
		// cooldown is 0,
		// we make sure it isn't for the same reason as the delay.
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
			// Anything below 3 milliseconds makes your computer sad from my
			// experience.
			if (dl >= 3) {
				new Timer().schedule(new Delay(this, key, p, event, cooledDown,
						task), dl);
			} else {
				sendMessage(p, cooledDown, key, event, task);
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
	/**
	 * Gets the message, and sends it if the player is a receiver.
	 */
	public void sendMessage(Player trigger, Collection<Player> possibleReceivers, String key, String event, CooldownTask task)
	{
		message.load();
		config.load();
		Set<Entry> receivers = getEntries(trigger, key, event, "receivers");
		String[] lines = this.getLines(event, key);
		for (Player receiver : possibleReceivers) {
			if (matchEntries(receiver, receivers)) {
				for (String str : lines) {
					receiver.sendMessage(process(str, trigger, event));
				}
			}
		}
		if (task != null) {
			task.trigger();
		}
	}

	public boolean notNull(String event) {
		message.load();
		String msgnode = "messages";
		List<String> messages = message.getKeys("messages");
		List<String> keys = message.getKeys("messages." + event);
		if(messages != null && keys != null && msgnode.length() != 0) {
			return true;
		} else {
			return false;
		}
	}
	
	// Begin basic event passing
	public void onPlayerJoin(PlayerJoinEvent event) {
		config.load();
		Player p = event.getPlayer();
			if(existingPlayer(p.getName()) && notNull("login")) {
				preProcessMessage(p, "login", "");
			} else if(notNull("firstlogin")) {
				preProcessMessage(p, "firstlogin", "");
			}

		if (config.getBoolean("clearjoinmsg", true)) {
			event.setJoinMessage(null);
		}
	}

	public void onPlayerQuit(PlayerQuitEvent event) {
		config.load();
		Player p = event.getPlayer();
		if(notNull("quit")) {
			preProcessMessage(p, "quit", "");
		}

		if (config.getBoolean("clearquitmsg", true)) {
			event.setQuitMessage(null);
		}
	}

	public void onPlayerKick(PlayerKickEvent event) {
		config.load();
		Player p = event.getPlayer();
		if(notNull("kick")) {
			preProcessMessage(p, "kick", "");
		}

		if (config.getBoolean("clearkickmsg", true)) {
			event.setLeaveMessage(null);
		}
	}

	// End basic event passing
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		message.load();
		Player p = event.getPlayer();
		String msg = event.getMessage();
		String cmd = msg.substring(1); // This is the bare command, without "/"
		if(notNull("command")) {
			List<String> commands = message.getKeys("messages.command");
			for (String key : commands) {
				if (key != null && msg.equalsIgnoreCase("/" + key)) {
					event.setCancelled(true);
					preProcessMessage(p, "command", cmd);
					break;
				}
			}
		}
	}
}
