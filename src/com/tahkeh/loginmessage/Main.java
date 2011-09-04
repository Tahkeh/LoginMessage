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
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.iConomy.iConomy;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.tahkeh.loginmessage.store.PropertiesFile;
import com.tahkeh.loginmessage.store.Store;

import cosine.boseconomy.BOSEconomy;

public class Main extends JavaPlugin //Main class, 'nuff said
{
	private Logger log;
	public static boolean directory = new File("plugins/LoginMessage/").mkdir();
	public Configuration config;
	public Configuration message;
	private Config cfg;
	public PropertiesFile prop;
	public static Message msg;
	public static PermissionHandler Permissions;
	public static Store store;
	public static String bpu = "BukkitPluginUtilities";
	public static String bpuname = "bukkitutil-1.1.0.jar";
	public static String bpupath = "http://cloud.github.com/downloads/xZise/Bukkit-Plugin-Utilties/" + bpuname;
	public static String bpudest = "lib" + File.separator + bpuname;
	static iConomy iConomy = null;
	static BOSEconomy bose = null;
	private static Server Server = null;
  
	//setupPermissions() method from Permissions API
	private void setupPermissions() {
		if(PermissionsEnabled() == true){
			Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
			if (Permissions == null) {
				if (permissionsPlugin != null) {
					Permissions = ((Permissions) permissionsPlugin).getHandler();
					} else {  
					}
				}
			} else {
			}
		}
	
	public void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_JOIN, msg, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, msg, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_KICK, msg, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, msg, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, new SListener(this), Event.Priority.Monitor, this);
        pm.registerEvent(Event.Type.PLUGIN_DISABLE, new SListener(this), Event.Priority.Monitor, this);
	}
	
	public void onDisable()
	{
		log.info("[LoginMessage] version " + this.getDescription().getVersion() + " disabled");
	}

  
	public void onEnable()
	{
		log = Logger.getLogger("Minecraft");
	    log.info("[LoginMessage] version " + this.getDescription().getVersion() + " enabled");
	    prop = new PropertiesFile("plugins" + File.separator + "LoginMessage" + File.separator + "store.txt");
	    config = new Configuration(new File(getDataFolder(), "config.yml"));
	    message = new Configuration(new File(getDataFolder(), "messages.yml"));
	    cfg = new Config(getDataFolder(), this);
	    cfg.setup();
	    config.load();
	    message.load();
	    store = new Store(this, prop);
	    store.load("enable");
	    msg = new Message(this, config, message, log, store);
	    if(PermissionsEnabled())
	    {
	    	setupPermissions();
	    }
	    if(getServer().getPluginManager().getPlugin("BOSEconomy") != null) {
	    	bose = (BOSEconomy) getServer().getPluginManager().getPlugin("BOSEconomy");
	    }
        cfg.setup();
        config.load();
        Server = getServer();
        downloadFile(bpupath, bpudest, bpu);
        registerEvents();
        }
	
	public void downloadFile(String urlpath, String dest, String file) {
		if(!new File(dest).exists()) {
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
				log.info("[LoginMessage] Successfully downloaded " + file + "!");
			} catch (IOException e) {
				log.severe("[LoginMessage] Something went wrong when downloading " + file + "!");
				if(file.equals(bpu)) {
					log.severe("[LoginMessage] Disabling LM...");
					getPluginLoader().disablePlugin(this);
				}
			}
		}
	}
  
  public boolean iConomyEnabled()
  {
	  return(config.getBoolean("useico", true) && getServer().getPluginManager().getPlugin("iConomy") != null); //return true if useico in config.yml is set to true and iConomy exists
  }
  
  public boolean BOSEconomyEnabled()
  {
	  return(config.getBoolean("useico", true) && getServer().getPluginManager().getPlugin("BOSEconomy") != null); //return true if useico in config.yml is set to true and BOSEconomy exists
  }
  
  public boolean PermissionsEnabled()
  {
	  return(config.getBoolean("useper", true) && getServer().getPluginManager().getPlugin("Permissions") != null); //return true if useper in config.yml is set to true and Permissions exists
  }
  
  public static Server getBukkitServer()
  {
      return Server;
  }
  
  public static BOSEconomy getBOSEconomy() {
	  return bose;
  }
  
  public static PermissionHandler getPermissions()
  {
	  return Permissions;
  }
  
  public static boolean setiConomy(iConomy plugin)
  {
      if (iConomy == null)
      {
          iConomy = plugin;
      }
      else
      {
          return false;
      }
      return true;
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