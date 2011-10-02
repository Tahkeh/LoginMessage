package com.tahkeh.loginmessage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.xzise.XLogger;

public class Config //For all your configuration needs
{

	private File folder;
	private XLogger log;

	public Config(File folder, Main instance, XLogger log)
	{
		this.folder = folder;
		this.log = log;
		}
	
	public void setup(){
		File cfg = new File(folder, "config.yml");
		File msg = new File(folder, "messages.yml");
		File list = new File(folder, "list.yml");
		if(!cfg.exists()) //If, for some reason, the player decides to delete their .yml files, LM will create defaults
		{
			try
			{
				cfg.createNewFile();
				FileWriter fw = new FileWriter(cfg);
				BufferedWriter o = new BufferedWriter(fw);
				o.write("#clearjoinmsg: If true, the default 'Player joined the game' will not be shown.\n");
				o.write("clearjoinmsg: true\n");
				o.write("\n");
				o.write("#clearquitmsg: If true, the default 'Player left the game' will not be shown (for disconnected players).\n");
				o.write("clearquitmsg: true\n");
				o.write("\n");
				o.write("#clearkickmsg: If true, the default 'Player left the game' will not be shown (for kicked players).\n");
				o.write("clearkickmsg: true\n");
				o.write("\n");
				o.write("#cleardeathmsg: If true, the default death messages will not be shown.\n");
				o.write("cleardeathmsg: true\n");
				o.write("\n");
				o.write("#plugins: Put 'null' or 'none' in these fields to prevent LoginMessage from using these plugins when they're installed.\n");
				o.write("plugins:\n");
				o.write("    permissions: ''\n");
				o.write("    economy: ''\n");
				o.write("\n");
				o.write("#autoload: When true, LoginMessage will automatically reload its files when an event is called.\n");
				o.write("autoload: true\n");
				o.write("\n");
				o.write("#separator: This is what will be used to separate advanced %ol codes.\n");
				o.write("#Keep all text within the single quotes.\n");
				o.write("separator: '%&%&'\n");
				o.write("#format: This is the formatting used for the %srtime code.\n");
				o.write("#Go to goo.gl/3nZ5y to learn how to properly format.\n");
				o.write("format: K:mm a z\n");
				o.write("\n");
				o.write("#These fields are what will be displayed if a GeoIP lookup fails.\n");
				o.write("cityfail: Ragetown\n");
				o.write("ccodefail: USL\n");
				o.write("cnamefail: United States of Lulz\n");
				o.write("zipfail: 09001\n");
				o.write("rcodefail: TF\n");
				o.write("rnamefail: Trollface\n");
				o.write("\n");
				o.write("#The following is what will display with the %time code depending on what time it actually is. Put in upper case or %Time will not work.\n");
				o.write("day: Day\n");
				o.write("sunset: Dusk\n");
				o.write("night: Night\n");
				o.write("sunrise: Dawn\n");
				o.write("\n");
				o.write("#istrue/isfalse: What to return for true/false codes (keep capitalized).\n");
				o.write("#Keep all text within the single quotes. You may use color codes here if you wish.\n");
				o.write("istrue: '&2Yes'\n");
				o.write("isfalse: '&4No'\n");
				o.write("\n");
				o.write("#status: Shows the text based status for the %status code. \"afk\" is only applicable to AdminCmd users.\n");
				o.write("#Keep all text within the single quotes. You may use color codes here if you wish.\n");
				o.write("status:\n");
				o.write("    online: '&2Online'\n");
				o.write("    offline: '&7Offline'\n");
				o.write("    afk: '&6AFK'\n");
				o.write("\n");
				o.write("#noplayerfound: What to return when a player types in a player name that does not exist (as a command argument).\n");
				o.write("#Keep all text within the single quotes. You may use color codes and the %nm code to return the non-existant player.\n");
				o.write("noplayerfound: '&cPlayer \"%nm\" does not exist!'"); // Add \n if more text is added past this point
				
				o.close();
				fw.close();
				
			}
			catch (IOException e)
			{
				log.severe("[LoginMessage] Error creating config.yml file.");
			}
		}
		if(!msg.exists())
		{
			try
			{
				msg.createNewFile();
				FileWriter fw = new FileWriter(msg);
				BufferedWriter o = new BufferedWriter(fw);
				o.write("#Create new messages here. See the wiki for help.\n");
				o.write("messages:\n");
				o.write("    login:\n");
				o.write("        motd:\n");
				o.write("            receivers:\n");
				o.write("                groups: [pri]\n");
				o.write("                users: []\n");
				o.write("                permissions: []\n");
				o.write("            triggers:\n");
				o.write("                groups: [pub]\n");
				o.write("                users: []\n");
				o.write("                permissions: []\n");
				o.write("            cooldown: 0\n");
				o.write("            delay: 500\n");
				o.write("            message:\n");
				o.write("                - 'Welcome back, %nm!'\n");
				o.write("                - 'Players online: %ol%&%&n:&f:&f'\n");
				o.write("        bc:\n");
				o.write("            receivers:\n");
				o.write("                groups: [-pub]\n");
				o.write("                users: []\n");
				o.write("                permissions: []\n");
				o.write("            triggers:\n");
				o.write("                groups: [pub]\n");
				o.write("                users: []\n");
				o.write("                permissions: []\n");
				o.write("            cooldown: 30\n");
				o.write("            delay: 500\n");
				o.write("            message:\n");
				o.write("                - '%nm logged in.'\n");
				o.write("    firstlogin:\n");
				o.write("        motd:\n");
				o.write("            receivers:\n");
				o.write("                groups: [pri]\n");
				o.write("                users: []\n");
				o.write("                permissions: []\n");
				o.write("            triggers:\n");
				o.write("                groups: [pub]\n");
				o.write("                users: []\n");
				o.write("                permissions: []\n");
				o.write("            cooldown: 0\n");
				o.write("            delay: 500\n");
				o.write("            message:\n");
				o.write("                - 'Welcome to the server, %nm!'\n");
				o.write("                - 'Players online: %ol%&%&n:&f:&f'\n");
				o.write("        bc:\n");
				o.write("            receivers:\n");
				o.write("                groups: [-pub]\n");
				o.write("                users: []\n");
				o.write("                permissions: []\n");
				o.write("            triggers:\n");
				o.write("                groups: [pub]\n");
				o.write("                users: []\n");
				o.write("                permissions: []\n");
				o.write("            cooldown: 30\n");
				o.write("            delay: 500\n");
				o.write("            message:\n");
				o.write("                - '%nm logged in.'\n");
				o.write("                - '%nm is visiting the server for the first time!'\n");
				o.write("    quit:\n");
				o.write("        qu:\n");
				o.write("            receivers:\n");
				o.write("                groups: [-pub]\n");
				o.write("                users: []\n");
				o.write("                permissions: []\n");
				o.write("            triggers:\n");
				o.write("                groups: [pub]\n");
				o.write("                users: []\n");
				o.write("                permissions: []\n");
				o.write("            cooldown: 0\n");
				o.write("            delay: 0\n");
				o.write("            message:\n");
				o.write("                - '%nm quit.'\n");
				o.write("    kick:\n");
				o.write("        ki:\n");
				o.write("            receivers:\n");
				o.write("                groups: [-pub]\n");
				o.write("                users: []\n");
				o.write("                permissions: []\n");
				o.write("            triggers:\n");
				o.write("                groups: [pub]\n");
				o.write("                users: []\n");
				o.write("                permissions: []\n");
				o.write("            cooldown: 0\n");
				o.write("            delay: 0\n");
				o.write("            message:\n");
				o.write("                - '%nm was kicked.'\n");
				o.write("    command:\n");
				o.write("        motd:\n");
				o.write("            receivers:\n");
				o.write("                groups: [pri]\n");
				o.write("                users: []\n");
				o.write("                permissions: []\n");
				o.write("            triggers:\n");
				o.write("                groups: [pub]\n");
				o.write("                users: []\n");
				o.write("                permissions: []\n");
				o.write("            cooldown: 0\n");
				o.write("            delay: 0\n");
				o.write("            message:\n");
				o.write("                - 'Welcome, %nm!'\n");
				o.write("                - 'Players online: %ol%&%&n:&f:&f'"); //Add \n if more text is added past this point
				
				o.close();
				fw.close();
			}
			catch (IOException e)
			{
				log.severe("[LoginMessage] Error creating messages.yml file.");
			}
		}
		if(!list.exists())
		{
			try
			{
				list.createNewFile();
				FileWriter fw = new FileWriter(list);
				BufferedWriter o = new BufferedWriter(fw);
				o.write("#Create new lists here. See the wiki for help.\n");
				o.write("lists:\n");
				o.write("    def:\n");
				o.write("        players:\n");
				o.write("            groups: [pub]\n");
				o.write("            users: []\n");
				o.write("            permissions: []\n");
				o.write("        format: '%nm'\n");
				o.write("        separator: ', '\n");
				o.write("    perm:\n");
				o.write("        players:\n");
				o.write("            groups: [pub]\n");
				o.write("            users: []\n");
				o.write("            permissions: []\n");
				o.write("        format: '%prefix%nm%suffix (%group)'\n");
				o.write("        separator: ', '\n");
				o.write("    all:\n");
				o.write("        players:\n");
				o.write("            groups: [pub]\n");
				o.write("            users: [-Player]\n");
				o.write("            permissions: []\n");
				o.write("        format: '&f[%Status&f] %nm'\n");
				o.write("        separator: ', '\n");
				o.write("        online: false"); //Add \n if more text is added past this point
				
				o.close();
				fw.close();
			}
			catch (IOException e)
			{
				log.severe("[LoginMessage] Error creating list.yml file.");
			}
	}
	}
	
}
