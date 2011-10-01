package com.tahkeh.loginmessage.store;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.bukkit.entity.Player;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;
import com.tahkeh.loginmessage.Main;

public class Store {
	private final Main plugin;
	private final PropertiesFile prop;
	
	public Store(Main plugin, PropertiesFile file) {
		this.plugin = plugin;
		this.prop = file;
	}
	
	public void load(String event) {
		// Load is its own method due to possibilities of other "load" operations in the future
		populateProperties(event);
	}
	
	public void populateProperties(String event) {
		File geoip = new File(plugin.getDataFolder(), "GeoLiteCity.dat");
		if(plugin.getServer().getOnlinePlayers().length > 0) {
			for(Player p : plugin.getServer().getOnlinePlayers()) {
				if(event.equals("quit") || event.contains("kick") || event.equals("firstlogin")) {
					Date now = new Date();
					prop.setLong(p.getName() + ".laston", now.getTime());
				}
				if(geoip.exists()) {
					String ip = isLocal(p) ? Main.getExternalIp().getHostAddress() : p.getAddress().getAddress().getHostAddress();
					LookupService ls = null;
					try {
						ls = new LookupService(geoip);
					} catch (IOException e) {
					}
					Location loc = ls.getLocation(ip);
					if(prop.getString(p.getName() + ".city").isEmpty() && loc.city != null) {
						prop.getString(p.getName() + ".city", loc.city);
					}
					if(prop.getString(p.getName() + ".ccode").isEmpty() && loc.countryCode != null) {
						prop.getString(p.getName() + ".ccode", loc.countryCode);
					}
					if(prop.getString(p.getName() + ".cname").isEmpty() && loc.countryName != null) {
						prop.getString(p.getName() + ".cname", loc.countryName);
					}
					if(prop.getString(p.getName() + ".zip").isEmpty() && loc.postalCode != null) {
						prop.getString(p.getName() + ".zip", loc.postalCode);
					}
					if(prop.getString(p.getName() + ".rcode").isEmpty() && loc.region != null) {
						prop.getString(p.getName() + ".rcode", loc.region);
					}
					if(prop.getString(p.getName() + ".rname").isEmpty() && loc.countryCode != null && loc.region != null) {
						String ccode = prop.getString(p.getName() + ".ccode", loc.countryCode);
						String rcode = prop.getString(p.getName() + ".rcode", loc.region);
						prop.getString(p.getName() + ".rname", regionName.regionNameByCode(ccode, rcode));
					}
					ls.close();
				}
			}
		}
	}
	
	public long getLastLogin(String p) {
		Date now = new Date();
		return prop.getLong(p + ".laston", now.getTime());
	}
	
	public String getLocation(String type, String p) {
		load("null");
		return prop.getString(p + "." + type, plugin.config.getProperty(type + "fail").toString());
	}
	
	public boolean isLocal(Player p) {
		boolean r = false;
		try {
			String localip = InetAddress.getLocalHost().getHostAddress();
			String ip = p.getAddress().getAddress().getHostAddress();
			String shortlocalip = localip.substring(0, localip.length() - 3); // The last 3 indexes
			String shortip = ip.substring(0, ip.length() - 3); // could differ, so we exclude them
			
			if(shortlocalip.contains(shortip) || shortip.contains(shortlocalip) || ip.equals("127.0.0.1")) {
				r = true;
			}
		} catch (UnknownHostException e) {
		}
		return r;
	}
}
