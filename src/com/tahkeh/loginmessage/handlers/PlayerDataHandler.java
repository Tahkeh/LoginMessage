package com.tahkeh.loginmessage.handlers;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;
import com.tahkeh.loginmessage.Main;
import com.tahkeh.loginmessage.store.PropertiesFile;

public class PlayerDataHandler {	
	public final PropertiesFile prop;
	public final File geoip;
	
	public PlayerDataHandler(PropertiesFile prop, File geoip) {
		this.prop = prop;
		this.geoip = geoip;
	}
	
	private String getRegionName(Player p, Location l, Map<String, String> geoipFail) {
		String ccode = "";
		String rcode = "";
		if (isEmpty(p, "ccode")) {
			rcode = lookupGeoIP(p, "ccode", geoipFail);
			storeString(p, "ccode", rcode);
		} else {
			rcode = getString(p, "ccode", geoipFail.get("ccode"));
		}
		if (isEmpty(p, "rcode")) {
			rcode = lookupGeoIP(p, "rcode", geoipFail);
			storeString(p, "rcode", rcode);
		} else {
			rcode = getString(p, "rcode", geoipFail.get("rcode"));
		}
		return regionName.regionNameByCode(ccode, rcode);
	}
	
	public static Player getPlayer(String p) {
		return Bukkit.getPlayerExact(p);
	}
	
	public static long getTime() {
		Date t = new Date();
		return t.getTime();
	}
	
	public boolean isEmpty(Player p, String type) {
		return prop.getString(p.getName() + "." + type, "").isEmpty();
	}
	
	public String lookupGeoIP(Player p, String type, Map<String, String> geoipFail) {
		String result = "";
		if (isEmpty(p, type)) {
			try {
				LookupService ls = new LookupService(geoip);
				Location l = ls.getLocation(getIP(p));
				//TODO: Find more efficient way of doing next part.
				if (type.equals("city")) {
					if (l.city == null) {
						result = geoipFail.get("city");
					} else {
						result = l.city;
					}
				}
				if (type.equals("ccode")) {
					if (l.countryCode == null) {
						result = geoipFail.get("ccode");
					} else {
						result = l.countryCode;
					}
				}
				if (type.equals("cname")) {
					if (l.countryName == null) {
						result = geoipFail.get("cname");
					} else {
						result = l.countryName;
					}
				}
				if (type.equals("zip")) {
					if (l.postalCode == null) {
						result = geoipFail.get("zip");
					} else {
						result = l.postalCode;
					}
				}
				if (type.equals("rcode")) {
					if (l.region == null) {
						result = geoipFail.get("rcode");
					} else {
						result = l.region;
					}
				}
				if (type.equals("rname")) {
					if (getRegionName(p, l, geoipFail) == null) {
						result = geoipFail.get("rname");
					} else {
						result = getRegionName(p, l, geoipFail);
					}
				}
			} catch (IOException e) {
				result = getString(p, type, geoipFail.get(type));
			}
		} else {
			result = getString(p, type, geoipFail.get(type));
		}
		storeString(p, type, result);
		return result;
	}
	
	public void storeString(Player p, String type, String value) {
		prop.setString(p.getName() + "." + type, value);
	}
	
	public String getString(Player p, String type, String fail) {
		return prop.getString(p.getName() + "." + type, fail);
	}
	
	public String getIP(Player p) {
		return isLocal(p) ? Main.getExternalIp().getHostAddress() : p.getAddress().getAddress().getHostAddress();
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
