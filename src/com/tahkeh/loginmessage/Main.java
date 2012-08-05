package com.tahkeh.loginmessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import com.tahkeh.loginmessage.listeners.MessageListener;
import com.tahkeh.loginmessage.store.MaterialTable;
import com.tahkeh.loginmessage.store.PropertiesFile;

import de.xzise.XLogger;
import de.xzise.wrappers.economy.EconomyHandler;
import de.xzise.wrappers.permissions.BufferPermission;
import de.xzise.wrappers.permissions.PermissionsHandler;

public class Main {
	private static BufferPermission<Boolean> RELOAD = BufferPermission.create("loginmessage.reload", false);

	private Config cfg;
	public PropertiesFile storeProperties;
	public File tableFile;
	public Message msg;
	public MaterialTable table;
	public LoginMessage plugin;

	private FileConfigurationPair<YamlConfiguration> configuration;
	private XLogger logger;
	private static PermissionsHandler permissions;
	private static EconomyHandler economy;
	
	public Main(LoginMessage plugin) {
		this.plugin = plugin;
	}

	public void onDisable() {
		if (logger != null) {
			logger.disableMsg();
		}
	}
	
	public void onEnable() {
		File folder = plugin.getDataFolder();
		folder.mkdir();
		logger = new XLogger(plugin);
		storeProperties = new PropertiesFile(new File(folder, "store.txt"), logger);
		tableFile = new File(folder, "items.txt");
		this.configuration = new FileConfigurationPair<YamlConfiguration>(new File(folder, "config.yml"), new YamlConfiguration(), "configuration", this.logger).load();
		FileConfigurationPair<YamlConfiguration> m = new FileConfigurationPair<YamlConfiguration>(new File(folder, "messages.yml"), new YamlConfiguration(), "messages", this.logger).load();
		FileConfigurationPair<YamlConfiguration> list = new FileConfigurationPair<YamlConfiguration>(new File(folder, "lists.yml"), new YamlConfiguration(), "lists", this.logger).load();
		cfg = new Config(folder, plugin, logger);
		cfg.setup();
		if (!tableFile.exists()) {
			MaterialTable.initialWrite(tableFile, logger);
		}
		table = new MaterialTable(tableFile, logger);
		msg = new Message(plugin, this.configuration, m, list, logger, table);
		msg.load("load");
		cfg.setup();
		PluginManager pm = plugin.getServer().getPluginManager();
		// Init handlers
		permissions = new PermissionsHandler(pm, this.configuration.fileConfiguration.getString("plugins.permissions"), this.logger);
		permissions.load();
		economy = new EconomyHandler(pm, this.configuration.fileConfiguration.getString("plugins.economy"), "", this.logger);
		economy.load();
		plugin.getServer().getPluginManager().registerEvents(new MessageListener(this.msg), plugin);
		plugin.getServer().getPluginManager().registerEvents(new SListener(), plugin);
		this.logger.enableMsg();
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
