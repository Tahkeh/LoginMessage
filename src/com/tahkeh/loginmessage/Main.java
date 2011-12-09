package com.tahkeh.loginmessage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.tahkeh.loginmessage.listeners.EListener;
import com.tahkeh.loginmessage.listeners.PListener;
import com.tahkeh.loginmessage.store.MaterialTable;
import com.tahkeh.loginmessage.store.PropertiesFile;
import com.tahkeh.loginmessage.store.Store;

import de.xzise.MinecraftUtil;
import de.xzise.XLogger;
import de.xzise.wrappers.WrapperServerListener;
import de.xzise.wrappers.economy.EconomyHandler;
import de.xzise.wrappers.permissions.BufferPermission;
import de.xzise.wrappers.permissions.PermissionsHandler;

public class Main extends JavaPlugin {
	public final static String BPU = "BukkitPluginUtilities";
	public final static String BPU_NAME = "bukkitutil-1.3.0.jar";
	public final static String BPU_PATH = "http://cloud.github.com/downloads/xZise/Bukkit-Plugin-Utilties/" + BPU_NAME;
	public final static String BPU_DEST = "lib" + File.separator + BPU + ".jar";

	public Configuration config;
	public Configuration message;
	public Configuration list;
	private Config cfg;
	public PropertiesFile storeProperties;
	public File tableFile;
	public Message msg;
	public Store store;
	public MaterialTable table;
	
	private XLogger logger;
	private static PermissionsHandler permissions;
	private static EconomyHandler economy;
	private BufferPermission<Boolean> reload;
	
	public void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, new PListener(msg), Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, new PListener(msg), Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_KICK, new PListener(msg), Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, new PListener(msg), Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DEATH, new EListener(msg), Event.Priority.Normal, this);
		WrapperServerListener.createAndRegisterEvents(this, permissions, economy);
	}

	public void onDisable()
	{
		getServer().getScheduler().cancelTasks(this);
		if (logger != null) {
			logger.disableMsg();
		}
	}

	private void update(String message) {
		Logger logger = Logger.getLogger("Minecraft");
		logger.info(message);
		if (downloadFile(BPU_PATH, BPU_DEST, BPU))
		{
			logger.info("[LoginMessage] Installed '" + BPU + "'. You need to reload the server!");
		} else {
			logger.severe("[LoginMessage] Unable to install '" + BPU + "'! Disabling plugin.");
		}
		this.getPluginLoader().disablePlugin(this);
	}

	public void onEnable() {
		try {
			if (MinecraftUtil.needUpdate(1, 3)) {
				this.update("Need to update Bukkit Plugin Utilities to at least 1.3.0! Downloading ...");
				return;
			}
		} catch (NoSuchMethodError e) {
			this.update("Need to update Bukkit Plugin Utilities to at least 1.3.0! Downloading ...");
			return;
		} catch (NoClassDefFoundError e) {
			this.update("No Bukkit Plugin Utilities found! Downloading ...");
			return;
		}

		File folder = getDataFolder();
		logger = new XLogger(this);
		reload = BufferPermission.create("loginmessage.reload", false);
		storeProperties = new PropertiesFile(new File(folder, "store.txt"), logger);
		tableFile = new File(folder, "items.txt");
		config = new Configuration(new File(folder, "config.yml"));
		message = new Configuration(new File(folder, "messages.yml"));
		list = new Configuration(new File(folder, "list.yml"));
		cfg = new Config(folder, this, logger);
		cfg.setup();
		config.load();
		message.load();
		store = new Store(this, storeProperties);
		if (!tableFile.exists()) {
			MaterialTable.initialWrite(tableFile, logger);
		}
		table = new MaterialTable(tableFile, logger);
		msg = new Message(this, config, message, list, logger, store, table);
		msg.load("load");
		cfg.setup();
		config.load();
		PluginManager pm = getServer().getPluginManager();
		// Init handlers
		permissions = new PermissionsHandler(pm, config.getString("plugins.permissions"), this.logger);
		permissions.load();
		economy = new EconomyHandler(pm, config.getString("plugins.economy"), "", this.logger);
		economy.load();
		registerEvents();
		this.logger.enableMsg();
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean player = false;
		if (sender instanceof Player) {
			player = true;
		}
		if (command.getName().equalsIgnoreCase("lmsg")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					if (permissions.permission(sender, reload)) {
						msg.unload();
						msg.load("load");
						sender.sendMessage(ChatColor.RED + "LoginMessage files reloaded.");
						return true;
					} else {
						sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
					}
				}
			} else if (permissions.permission(sender, reload)) { //TODO: Add OR statements when new permissions are added.
				sender.sendMessage(ChatColor.RED + "Usage:");
				if (permissions.permission(sender, reload)) {
					sender.sendMessage(ChatColor.RED + String.format("%s", player ? "/" : "") + label.toLowerCase() + " reload - Reloads LoginMessage files.");
				}
			}
		}
		return false;
	}
	
	public static boolean downloadFile(String urlpath, String dest, String file) {
		File destFile = new File(dest).getAbsoluteFile();
		if(!destFile.exists()) {
			final File parentDirectory = destFile.getParentFile();
			final boolean directoryCreated;
			if (parentDirectory != null) {
				if (!parentDirectory.exists()) {
					directoryCreated = parentDirectory.mkdirs();
				} else {
					directoryCreated = true;
				}
			} else {
				directoryCreated = false;
			}

			Logger log = Logger.getLogger("Minecraft");
			if (directoryCreated) {
				log.info("[LoginMessage] Downloading " + file + "...");
				try {
					URL url = new URL(urlpath);
					URLConnection con = url.openConnection();
					BufferedOutputStream o = new BufferedOutputStream(new FileOutputStream(dest));
					BufferedInputStream in = new BufferedInputStream(con.getInputStream());
					byte[] buffer = new byte[Integer.parseInt(url.openConnection().getHeaderField("Content-Length"))];
					long count = 0;
					int n = 0;

					while (-1 != (n = in.read(buffer))) {
						o.write(buffer, 0, n);
						count += n;
					}
					o.flush();
					o.close();
					log.info("[LoginMessage] Successfully downloaded " + file + " (" + getBinaryPrefixValue(count) + "B) !");
				} catch (Throwable e) {
					log.log(Level.SEVERE, "[LoginMessage] Something went wrong when downloading " + file + "!", e);
					return false;
				}
			} else {
				log.severe("[LoginMessage] Unable to create directory" + (parentDirectory != null ? ": " + parentDirectory.getAbsolutePath() : " (no parent directory)!"));
				return false;
			}
		}
		return true;
	}

	// Could be accessed also with BPU 1.3
	private static String getBinaryPrefixValue(long value) {
		final int ONE_ITERATION = 1024; // 2¹⁰
		final String[] PREFIXES = new String[] { "", "Ki", "Mi", "Gi", "Ti", "Pi", "Ei", "Zi", "Yi" };

		int iterations = 0;
		while (value > ONE_ITERATION && iterations < PREFIXES.length - 1) {
			value >>= 10;
			iterations++;
		}

		return value + " " + PREFIXES[iterations];
	}

  public static EconomyHandler getEconomy()
  {
      return economy;
  }
  
  public static PermissionsHandler getPermissions()
  {
	  return permissions;
  }
		
  public static InetAddress getExternalIp(){ //Method to get the IP of the local computer. Courtesy of NateLogan.
	  
	  InetAddress ip = null;

      //Primary site:
      ip = parseExternalIP("http://automation.whatismyip.com/n09230945.asp");
      //Alternative site:
      if(ip == null) ip = parseExternalIP("http://checkip.dyndns.com/");
      //local IP:
      if(ip == null){
          try {
              ip = Inet4Address.getLocalHost();
          } catch (UnknownHostException ex) {
              //Unknown exception, will return null;
          }
      }

      return ip;
      }

  private static InetAddress parseExternalIP(String url){ //If first IP check returns null, this method is used. Courtesy of NateLogan.
      InputStream inputStream = null;
      BufferedReader bufferedReader = null;
      InetAddress ip = null;

      try {
          Pattern pattern = Pattern.compile("(([\\d]{1}){1,3}\\.){3}([\\d]{1}){1,3}");    //IP address regex
          inputStream = (new URL(url)).openStream();
          bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
          Matcher matcher = pattern.matcher(bufferedReader.readLine());

          if (matcher.find()) {   //finds first IP address in first line of url stream
              ip = Inet4Address.getByName(matcher.group());
              //System.out.println(matcher.group());  //(TEST) print raw address
          }
      } catch (Exception ex) {
         return null;
      } finally {
          try {
              inputStream.close();
          } catch (Exception ex) {}
          try {
              bufferedReader.close();
          } catch (Exception ex) {}
      }

      return ip;
  }
}