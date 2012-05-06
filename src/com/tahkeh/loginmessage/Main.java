package com.tahkeh.loginmessage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.tahkeh.loginmessage.listeners.MessageListener;
import com.tahkeh.loginmessage.store.MaterialTable;
import com.tahkeh.loginmessage.store.PropertiesFile;

import de.xzise.XLogger;
import de.xzise.wrappers.economy.EconomyHandler;
import de.xzise.wrappers.permissions.BufferPermission;
import de.xzise.wrappers.permissions.PermissionsHandler;

public class Main extends JavaPlugin {
	public final static String BPU = "BukkitPluginUtilities";
	public final static String BPU_NAME = "bukkitutil-1.2.3.jar";
	public final static String BPU_PATH = "http://cloud.github.com/downloads/xZise/Bukkit-Plugin-Utilties/" + BPU_NAME;
	public final static String BPU_DEST = "lib" + File.separator + BPU + ".jar";

	private final static BufferPermission<Boolean> RELOAD = BufferPermission.create("loginmessage.reload", false);

	private Config cfg;
	public PropertiesFile storeProperties;
	public File tableFile;
	public Message msg;
	public MaterialTable table;

	private FileConfigurationPair<YamlConfiguration> configuration;
	private XLogger logger;
	private static PermissionsHandler permissions;
	private static EconomyHandler economy;

	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
		if (logger != null) {
			logger.disableMsg();
		}
	}

	@Override
	public void onEnable() {
		File folder = getDataFolder();
		folder.mkdir();
		if (downloadFile(BPU_PATH, BPU_DEST, BPU))
		{
			try {
				logger = new XLogger(this);
			} catch(NoClassDefFoundError e) {
				Logger.getLogger("Minecraft").info("[LoginMessage] Reload the server!");
				this.getPluginLoader().disablePlugin(this);
				return;
			}

			storeProperties = new PropertiesFile(new File(folder, "store.txt"), logger);
			tableFile = new File(folder, "items.txt");
			this.configuration = new FileConfigurationPair<YamlConfiguration>(new File(getDataFolder(), "config.yml"), new YamlConfiguration(), "configuration", this.logger).load();
			FileConfigurationPair<YamlConfiguration> m = new FileConfigurationPair<YamlConfiguration>(new File(getDataFolder(), "messages.yml"), new YamlConfiguration(), "messages", this.logger).load();
			FileConfigurationPair<YamlConfiguration> list = new FileConfigurationPair<YamlConfiguration>(new File(getDataFolder(), "lists.yml"), new YamlConfiguration(), "lists", this.logger).load();
			cfg = new Config(folder, this, logger);
			cfg.setup();
			if (!tableFile.exists()) {
				MaterialTable.initialWrite(tableFile, logger);
			}
			table = new MaterialTable(tableFile, logger);
			msg = new Message(this, this.configuration, m, list, logger, table);
			msg.load("load");
			cfg.setup();
			PluginManager pm = getServer().getPluginManager();
			// Init handlers
			permissions = new PermissionsHandler(pm, this.configuration.fileConfiguration.getString("plugins.permissions"), this.logger);
			permissions.load();
			economy = new EconomyHandler(pm, this.configuration.fileConfiguration.getString("plugins.economy"), "", this.logger);
			economy.load();
			this.getServer().getPluginManager().registerEvents(new MessageListener(this.msg), this);
			this.getServer().getPluginManager().registerEvents(new SListener(), this);
			this.logger.enableMsg();
		} else {
			Logger.getLogger("Minecraft").severe("[LoginMessage] Unable to install '" + BPU + "'! Disabling plugin.");
			this.getPluginLoader().disablePlugin(this);
		}
	}
	
	public void readUsage(CommandSender sender, String alias) {
		sender.sendMessage(ChatColor.RED + "Usage:");
		if (permissions.permission(sender, RELOAD)) {
			sender.sendMessage(ChatColor.RED + alias + " reload - Reloads LoginMessage files.");
		}
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String noPerm = ChatColor.RED + "You don't have permission to do that!";
		boolean player = false;
		if (sender instanceof Player) {
			player = true;
		}
		String alias = String.format("%s", player ? "/" : "") + label.toLowerCase();
		if (command.getName().equalsIgnoreCase("lmsg")) {
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					if (permissions.permission(sender, RELOAD)) {
						msg.unload();
						msg.load("load");
						sender.sendMessage(ChatColor.RED + "LoginMessage files reloaded.");
						return true;
					} else {
						sender.sendMessage(noPerm);
					}
				}
			} else if (permissions.permission(sender, RELOAD)) {
				readUsage(sender, alias);
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

	public FileConfigurationPair<YamlConfiguration> getConfiguration() {
		return this.configuration;
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
