package com.tahkeh.loginmessage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.config.Configuration;

import com.iConomy.iConomy;
import com.iConomy.system.Holdings;
import com.maxmind.geoip.*;
import com.nijiko.permissions.PermissionHandler;

import com.tahkeh.loginmessage.sub.Cooldown;
import com.tahkeh.loginmessage.sub.Delay;
import com.tahkeh.loginmessage.sub.Entry;
import com.tahkeh.loginmessage.sub.Group;
import com.tahkeh.loginmessage.sub.User;

public class Message extends PlayerListener //Handles everything message-related (so 90% of this plugin)
{
	private final Main plugin;
	private final Configuration config;
	private final Configuration message;
	private final Logger log;
	String separator = "%&%&"; //Currently a static string until I can figure out how to get YML to read characters as a string
	Map<String, Boolean> cooldownvalues = new HashMap<String, Boolean>(); //Stores the cooldown keys, value in a Map - to be accessed by Cooldown class
	Map<String, Boolean> cdwaiting = new HashMap<String, Boolean>(); //Like cooldownvalues, but Boolean is for whether or not a timer is running
	
	public Message(Main instance, Configuration config, Configuration message, Logger log)
	{
		this.plugin = instance;
		this.config = config;
		this.message = message;
		this.log = log;
		}
	
	public String getTime(Long rawtime, boolean caps) //Neat little method to get the text-based version of the time!
	{
		  String Day = config.getString("day");
		  String day = Day.toLowerCase();
		  
		  String Dusk = config.getString("sunset");
		  String dusk = Dusk.toLowerCase();
		  
		  String Dawn = config.getString("sunrise");
		  String dawn = Dawn.toLowerCase();
		  
		  String Night = config.getString("night");
		  String night = Night.toLowerCase();
		  
		  if(caps == false)
		  {
			  if(rawtime == 24000 || rawtime <= 11999)
			  {
				  return day;
			  }
			  else if(rawtime == 12000 || rawtime <= 12999){
				  return dusk;
			  }
			  else if(rawtime == 13000 || rawtime <= 22999){
				  return night;
			  }
			  else if(rawtime == 23000 || rawtime <= 23999){
				  return dawn;
			  }
		  }
		  else if(rawtime == 24000 || rawtime <= 11999){
			  return Day;
		  }
		  else if(rawtime == 12000 || rawtime <= 12999){
			  return Dusk;
		  }
		  else if(rawtime == 13000 || rawtime <= 22999){
			  return Night;
		  }
		  else if(rawtime == 23000 || rawtime <= 23999){
			  return Dawn;
		  }
		return "";
	  }
	
	public String olProcess(String str, Player player, String event) //Method for processing %ol code
	{
		int on = 0;
		String list = "";
		String s = str;
        
        while(s.indexOf(separator) >= 0)
        {
        	s = s.substring(s.indexOf(separator) + 1);
        }
        Player[] online = plugin.getServer().getOnlinePlayers();
        int length = online.length - 1;
        List<Player> all_list = new ArrayList<Player>();
        for(Player all : online){
        		while(!all_list.contains(all)){
            		all_list.add(all);
            		//Create a list of all players that we can remove stuff from
            	}
        		if(isLeaveEvent(event)){
        			all_list.remove(player);
        			length = length - 1;
        		}
        }
        
       if(s.substring(separator.length() - 1, s.indexOf(":")).length() == 1 || s.substring(separator.length() - 1, s.indexOf(":")).length() == 0){
    	   PermissionHandler handler = Main.getPermissions();
    	   //Credit to mathmaniac43 for awesome string trimming with substring()
    	   String a = s.substring(separator.length() - 1, s.indexOf(":"));
           s = s.substring(s.indexOf(":") + 1);

           String b = s.substring(0, s.indexOf(":"));
           s = s.substring(s.indexOf(":") + 1);

           String c = s.substring(0, 2);
           boolean sf1 = false;
           boolean pr1 = false;
           
           boolean sf2 = false;
           boolean pr2 = false;
           
           if(b.equalsIgnoreCase("sf")){
        	   sf1 = true;
        	   pr1 = false;
           } else if(b.equalsIgnoreCase("pr")){
        	   sf1 = false;
        	   pr1 = true;
           }
           if(c.equalsIgnoreCase("sf")){
        	   sf2 = true;
        	   pr2 = false;
           } else if(c.equalsIgnoreCase("pr")){
        	   sf2 = false;
        	   pr2 = true;
           }
    	       //TODO Find better way to get pr and sf working; this is too involved
    	    	   for(Player current : all_list){
    	    		   String b1 = "";
    	    		   String c1 = "";
    	    		   if(plugin.PermissionsEnabled())
    	    		   { //This is incredibly long (just FYI)
    	    			   b1 = (sf1 && !pr1 && plugin.PermissionsEnabled() ? handler.getGroupSuffix(current.getWorld().getName(), handler.getGroup(current.getWorld().getName(), current.getName())) : handler.getGroupPrefix(current.getWorld().getName(), handler.getGroup(current.getWorld().getName(), current.getName())));
        	    		   c1 = (sf2 && !pr2 && plugin.PermissionsEnabled() ? handler.getGroupSuffix(current.getWorld().getName(), handler.getGroup(current.getWorld().getName(), current.getName())) : handler.getGroupPrefix(current.getWorld().getName(), handler.getGroup(current.getWorld().getName(), current.getName())));
    	    		   }
    	    		   if(!sf1 && !pr1){
    	    			   b1 = b; //If we're using pr/sf, then the string will be b1, which is defined earlier
    	    		   }
    	    		   if(!sf2 && ! pr2){
    	    			   c1 = c; //Same deal as before
    	    		   }
    	    		   b1 = b1.replaceAll("(&([a-z0-9]))", "¤$2"); //My good buddy replace-ampersands-with-simoleon-symbols regex
    	    		   c1 = c1.replaceAll("(&([a-z0-9]))", "¤$2"); //When you think about it, it makes sense. $2 is the a-z/0-9 character
        	            	  if(a.equals("d")){
        	            		  list = list + (on >= length ? b1 + current.getDisplayName() : new StringBuilder().append(b1 + current.getDisplayName()).append(c1 + ", ").toString());
        	  	                  on++;
        	            	  } else {
        	            		  list = list + (on >= length ? b1 + current.getName() : new StringBuilder().append(b1 + current.getName()).append(c1 + ", ").toString());
        	  	                  on++;
        	            	  }
    	    	   }

    	       String ol = "%ol" + separator + a + ":" + b + ":" + c; //This is the string that people actually type in their messages
    	       str = str.replaceAll(ol, list); //Like any other code, replace this with the proper regex
       }
		return str;
	}
	
	public boolean isLeaveEvent(String event) //For %ol and %size
	{
		return event.equals("kick") || event.equals("quit");
	}
	
	public String process(String str, Player player, String event){ //AVERT YOUR EYES, THIS IS ONE MESSY METHOD
		config.load();
		message.load();
		File geoip = new File(plugin.getDataFolder(), "GeoLiteCity.dat");
		String[] geoipcodes = {"%city", "%ccode", "%cname", "%zip", "%region"};
		Player[] online = plugin.getServer().getOnlinePlayers();
		int serverlist = online.length;
		if(isLeaveEvent(event)){
			serverlist = serverlist - 1;
		}
		//TODO Clean up, clean up, everybody do your share - this is a wreck
		String serverliststring = Integer.toString(serverlist);
	      int servermax = plugin.getServer().getMaxPlayers();
	      String servermaxstring = Integer.toString(servermax);
		  str = str.replaceAll("%dpnm", player.getDisplayName());
	      str = str.replaceAll("%nm", player.getName());
	      str = str.replaceAll("%size", serverliststring);
	      str = str.replaceAll("%max", servermaxstring);
	      if(str.contains("%ol" + separator))
	      {
	    	  str = olProcess(str, player, event);
	      }
	      if (plugin.iConomyEnabled())
	      {
	    	  if(!iConomy.hasAccount(player.getName()))
	    	  {
	    		  str = str.replaceAll("%bal", "");
	    		  }
			Holdings balance = iConomy.getAccount(player.getName()).getHoldings();
	        str = str.replaceAll("%bal", balance.toString());
	      }
	      if (plugin.PermissionsEnabled())
	      {
	    	PermissionHandler handler = Main.getPermissions();
	        String groupname = handler.getGroup(player.getWorld().getName(), player.getName());
	        str = str.replaceAll("%group", groupname);
	        
	        if (handler.getGroupPrefix(player.getWorld().getName(), groupname) != null || handler.getGroupSuffix(player.getWorld().getName(), groupname) != null)
	        {
	          String prefix = handler.getGroupPrefix(player.getWorld().getName(), groupname);
	          String suffix = handler.getGroupSuffix(player.getWorld().getName(), groupname);
	          String permissionslist = "";
	          int length1 = online.length - 1;
	          int on1 = 0;
	          for (Player current : online) {
	            if (current == null)
	            {
	            	on1++;
	            }
	            else
	            {
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
	      if(!plugin.isLocal(player)){
	    	  str = str.replaceAll("%ip", ip);
	    	  }
	      else if(str.contains("%ip"))
	      {
	    	  System.out.println("contains %ip");
	    	  str = str.replaceAll("%ip", Main.getExternalIp().getHostAddress());  
	    	  }
	    	  for(String g : geoipcodes){
	    		  if(str.contains(g)){
	    			  if(geoip.exists()){
				    	  Location loc = null;
				    	  LookupService location = null;
								try {
									location = new LookupService(geoip);
								} catch (IOException e) {
									log.info("[LoginMessage] Could not initiate GeoIP lookup service.");
								}
						      loc = location.getLocation(ip);
							      if(!plugin.isLocal(player)){
								      str = str.replaceAll("%city", loc.city);
								      str = str.replaceAll("%ccode", loc.countryCode);
								      str = str.replaceAll("%cname", loc.countryName);
								      str = str.replaceAll("%zip", loc.postalCode);
								      str = str.replaceAll("%region", loc.region);
							      } else {
								    	  str = str.replaceAll("%city", config.getString("local.city"));
								    	  str = str.replaceAll("%ccode", config.getString("local.countrycode"));
								    	  str = str.replaceAll("%cname", config.getString("local.countryname"));
								    	  str = str.replaceAll("%zip", config.getString("local.zip"));
								    	  str = str.replaceAll("%region", config.getString("local.region"));
								    	  }
							      location.close();
							      }
	    		  }
	    	  }
			      Long rawtime = player.getWorld().getTime();
			      String rtime = rawtime.toString();
			      Calendar cal = Calendar.getInstance();
			      SimpleDateFormat sdf = new SimpleDateFormat(config.getString("format"));
			      str = str.replaceAll("%world", player.getWorld().getName());
			      str = str.replaceAll("%rtime", rtime);
			      str = str.replaceAll("%time", getTime(rawtime, false));
			      str = str.replaceAll("%Time", getTime(rawtime, true));
			      str = str.replaceAll("%srtime", sdf.format(cal.getTime()));
			      str = str.replaceAll("(&([a-z0-9]))", "¤$2");
				  str = str.replaceAll("%sp", "");
		  return str;
	  }
	  
	
	public Set<Entry> getEntries(Player p, Player all, String key, String event, String type) //For receivers/triggers
	{
		message.load();
		Set<Entry> entries = new HashSet<Entry>();
		String path = "messages." + event;
		String keypath = path + "." + key;
		String userpath = keypath + "." + type + ".users";
		String grouppath = keypath + "." + type + ".groups";
		
		List<String> users = message.getStringList(userpath, null);
		List<String> groups = message.getStringList(grouppath, null);
		
		for(String g : groups)
		{
			if(g.equalsIgnoreCase("pub") && !g.equalsIgnoreCase("-pub"))
			{
				if(type.equalsIgnoreCase("receivers"))
				{
					entries.add(new User(all.getName()));
				}
				else //Things work differently for triggers - you only want the player triggering the event to be added to the list
				{
					entries.add(new User(p.getName()));
				}
			}
			if(g.equalsIgnoreCase("-pub"))
			{
				if(!all.equals(p))
				{
					entries.add(new User(all.getName()));
					break;
				}
			}
			if(g.equalsIgnoreCase("op"))
			{
				if(type.equalsIgnoreCase("receivers") && all.isOp())
				{
					entries.add(new User(all.getName()));
				}
				else if(type.equalsIgnoreCase("triggers") && p.isOp())
				{
					entries.add(new User(p.getName()));
				}
			}
			if(plugin.PermissionsEnabled())
			{
				entries.add(new Group(g, Main.getPermissions().getGroup(all.getWorld().getName(), all.getName())));
			}
			if(g.equalsIgnoreCase("pri"))
			{
				entries.add(new User(p.getName()));
				break;
			}
		}

		for(String u : users)
		{
			for(Entry e : entries)
			{
				if(!e.match(p))
				{
					entries.add(new User(u));
					break;
				}
			}
		}
		
		return entries;
	}
	
	public void preProcessMessage(Player p, String event, String cmdkey) //Check - is the triggerer listed as a trigger?
	{
		message.load();
		config.load();
		boolean cont = true;
		
		for(Player all : plugin.getServer().getOnlinePlayers())
		{
			for(String key : message.getKeys("messages." + event))
			{
				if(event.equals("command") && !cmdkey.equals(key)) //We don't want all messages showing up for just one command
				{
					cont = false;
				}
				else
				{
					cont = true;
				}
				if(cont)
				{
					Set<Entry> triggers = getEntries(p, all, key, event, "triggers");
					for(Entry t : triggers)
					{
						if(t.match(p))
						{
							finishMessage(p, all, event, key);
						}
						}
					}
				}
			}
	}
	
	public void setCooldownValues(String cdstr, boolean bool) //To be accessed from Cooldown class
	{
		cooldownvalues.put(cdstr, bool);
	}
	
	public void setCooldownWaiting(String cdstr, boolean bool) //To be accessed from Cooldown class
	{
		cdwaiting.put(cdstr, bool);
	}
	
	public void finishMessage(Player p, Player all, String event, String key) //Final touches - delay and cooldown
	{
		message.load();
		String keypath = "messages." + event + "." + key;
		Timer cooldown = new Timer();
		Timer delay = new Timer();
		int cd = message.getInt(keypath + ".cooldown", 0) * 1000;
		int dl = message.getInt(keypath + ".delay", 0);
		boolean cont = false;
		String cdstr = all.getName() + "." + event + "." + key; //Convoluted way of having multiple keys in one - format them into a single string!
		
		//Since the only number lower than 1000 that is possible for the cooldown is 0,
		//we make sure it isn't for the same reason as the delay.
		if(cd > 0)
		{
			if(cooldownvalues.get(cdstr) == null)
			{
				cont = true;
				setCooldownValues(cdstr, true);
				setCooldownWaiting(cdstr, false);
			}
			else if(cooldownvalues.get(cdstr))
			{
				cont = true;
			}
			else if(!cdwaiting.get(cdstr)) //Don't want it to schedule another timer if one is already running, now do we?
			{
				cooldown.schedule(new Cooldown(this, cdstr), cd);
				setCooldownWaiting(cdstr, true);
			}
		}
		else
		{
			cont = true;
		}
		//Check if the delay isn't greater than or equal to 3.
		//Anything below 3 milliseconds makes your computer sad from my experience.
		if(dl >= 3 && cont)
		{
			delay.schedule(new Delay(this, key, all, p, event), dl);
		}
		else if(cont)
		{
			sendMessage(all, p, key, event);
			setCooldownValues(cdstr, false);
		}
		//SO MANY ELSE CASES
	}
	public void sendMessage(Player all, Player p, String key, String event) //The grand finale - get the right message, and if the player is a receiver, send it!
	{
		message.load();
		config.load();
		Set<Entry> receivers = getEntries(p, all, key, event, "receivers");
		for(String str : message.getStringList("messages." + event + "." + key + ".message", null))
		{
			if(!str.isEmpty() || str != null)
			{
				for(Entry r : receivers)
				{
					if(r.match(all))
					{
						all.sendMessage(process(str, p, event));
						break;
					}
				}
				}
			}
	}
	//Begin basic event passing
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		config.load();
		Player p = event.getPlayer();
		preProcessMessage(p, "login", "");
		
		if(config.getBoolean("clearjoinmsg", true))
		{
			event.setJoinMessage(null);
		}
	}
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		config.load();
		Player p = event.getPlayer();
		preProcessMessage(p, "quit", "");
		
		if(config.getBoolean("clearquitmsg", true))
		{
			event.setQuitMessage(null);
		}
	}
	public void onPlayerKick(PlayerKickEvent event)
	{
		config.load();
		Player p = event.getPlayer();
		preProcessMessage(p, "kick", "");
		
		if(config.getBoolean("clearkickmsg", true))
		{
			event.setLeaveMessage(null);
		}
	}
	//End basic event passing
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) //This is a bit trickier, because we need to make sure the command typed is an LM command
	{
		message.load();
		Player p = event.getPlayer();
		String msg = event.getMessage();
		String cmd = msg.substring(1); //This is the bare command, without "/"
		List<String> commands = message.getKeys("messages.command");
		for(String key : commands)
		{
			if(msg.equalsIgnoreCase("/" + key)) //If what you typed == an LM command message, go right ahead!
			{
				event.setCancelled(true); //TODO If I don't do this, it says "Unknown command". I guess I should use onCommand in Main class?
				preProcessMessage(p, "command", cmd); //Here we actually use that preProcessMessage parameter
				break;
			}
		}
	}
}
