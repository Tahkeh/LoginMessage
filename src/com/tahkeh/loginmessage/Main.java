package com.tahkeh.loginmessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.iConomy.iConomy;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Main extends JavaPlugin //Main class, 'nuff said
{
	
	//Create variables
	private Logger log;
	public static boolean directory = new File("plugins/LoginMessage/").mkdir();
	public Configuration config;
	public Configuration message;
	private Config cfg;
	public static Message msg;
	public static PermissionHandler Permissions;
	static iConomy iConomy = null;
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

	public void onDisable()
	{
		log.info("[LoginMessage] version " + this.getDescription().getVersion() + " disabled");
	}

  
	public void onEnable()
	{
		log = Logger.getLogger("Minecraft");
	    log.info("[LoginMessage] version " + this.getDescription().getVersion() + " enabled");
	    config = new Configuration(new File(getDataFolder(), "config.yml"));
	    message = new Configuration(new File(getDataFolder(), "messages.yml"));
	    cfg = new Config(getDataFolder(), this);
	    cfg.setup();
	    config.load();
	    message.load();
	    msg = new Message(this, config, message, log);
	    if(PermissionsEnabled())
	    {
	    	setupPermissions();
	    }
        cfg.setup();
        config.load();
        Server = getServer();
        //Register events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_JOIN, msg, Event.Priority.Monitor, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, msg, Event.Priority.Monitor, this);
        pm.registerEvent(Event.Type.PLAYER_KICK, msg, Event.Priority.Monitor, this);
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, msg, Event.Priority.Monitor, this);
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, new SListener(this), Event.Priority.Monitor, this);
        pm.registerEvent(Event.Type.PLUGIN_DISABLE, new SListener(this), Event.Priority.Monitor, this);
        if(new File(getDataFolder(), "GeoLiteCity.dat").exists())
        {
        	fillLocalFields(); //If GeoLiteCity.dat is in LoginMessage folder, fill out local fields with fillLocalFields()
        	}
        }
  
	public boolean localNotFilled()
	{
		config.load();
		//Get local fields
	    String ccode = config.getString("local.countrycode", "");
	    String cname = config.getString("local.countryname", "");
	    String city = config.getString("local.city", "");
	    String region = config.getString("local.region", "");
	    String zip = config.getString("local.zip", "");
	    return(ccode.equals("") || cname.equals("") || city.equals("") || region.equals("") || zip.equals("")); //return true if any local field is not filled out
  }
  
	public boolean isLocal(Player p)
	{
		config.load();
		return(config.getString("local.players", "").contains(p.getName())); //Return true if p is in local.players in config.yml
		}
	
	public void fillLocalFields()
	{
		config.load();
		File geoip = new File(getDataFolder(), "GeoLiteCity.dat");
		if(geoip.exists()){
			if(localNotFilled()){ //If GeoLiteCity.dat is in LoginMessage folder and the local fields are not filled
				log.info("[LoginMessage] Attempting to fill in local fields...");
				try {
					InetAddress ip = getExternalIp();
					LookupService location = new LookupService(geoip, LookupService.GEOIP_MEMORY_CACHE);
					Location loc = location.getLocation(ip);
					//Fill local fields with data from GeoIP
					config.setProperty("local.countrycode", loc.countryCode);
					config.setProperty("local.countryname", loc.countryName);
					config.setProperty("local.city", loc.city);
					config.setProperty("local.region", loc.region);
					config.setProperty("local.zip", loc.postalCode);
					config.save();
					log.info("[LoginMessage] Successfully filled in local fields!");
					location.close();
					} catch (Exception e) {
						log.info("[LoginMessage] Unable to fill in local fields.");
						}
					}
			}
		}
  
  public boolean iConomyEnabled()
  {
	  return(config.getBoolean("useico", true) && getServer().getPluginManager().getPlugin("iConomy") != null); //return true if useico in config.yml is set to true and iConomy exists
  }
  
  public boolean PermissionsEnabled()
  {
	  return(config.getBoolean("useper", true) && getServer().getPluginManager().getPlugin("Permissions") != null); //return true if useper in config.yml is set to true and Permissions exists
  }
  
  public static Server getBukkitServer()
  {
      return Server;
  }

  public static iConomy getiConomy()
  {
      return iConomy;
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