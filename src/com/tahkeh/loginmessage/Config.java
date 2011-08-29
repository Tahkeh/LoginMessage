package com.tahkeh.loginmessage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class Config //For all your configuration needs
{
	private File folder;
	private Logger log;

	public Config(File folder, Logger log)
	{
		this.folder = folder;
		this.log = log;
	    }
	
	public void setup(){
		File cfg = new File(folder, "config.yml");
		File msg = new File(folder, "messages.yml");
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
				o.write("#useico: If false, iConomy will not be used, even if it's in the plugins folder.\n");
				o.write("useico: true\n");
				o.write("\n");
				o.write("#useper: If false, Permissions will not be used, even if it's in the plugins folder.\n");
				o.write("useper: true\n");
				o.write("\n");
				o.write("#format: This is the formatting used for the %srtime code.\n");
				o.write("#Go to goo.gl/3nZ5y to learn how to properly format.\n");
				o.write("format: K:mm a z\n");
				o.write("\n");
				o.write("#These fields are what will be displayed if a GeoIP lookup fails. You may use % codes and colors here.\n");
				o.write("#Feel free to put your own witty twist on things here!\n");
				o.write("cityfail: ACity\n");
				o.write("ccodefail: AC\n");
				o.write("cnamefail: A Country\n");
				o.write("zipfail: 00000\n");
				o.write("rcodefail: S/P\n");
				o.write("rnamefail: State/Province\n");
				o.write("\n");
				o.write("#The following is what will display with the %time code depending on what time it actually is. Put in upper case or %Time will not work.\n");
				o.write("day: Day\n");
				o.write("sunset: Dusk\n");
				o.write("night: Night\n");
				o.write("sunrise: Dawn"); //Add \n if more text is added past this point
				
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
				o.write("#Create new messages here. See the readme file for help.\n");
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
				o.write("            cooldown: 0\n");
				o.write("            delay: 500\n");
				o.write("            message:\n");
				o.write("                - '%nm logged in for the first time!'\n");
				o.write("                - 'Welcome %nm to the server!'\n");
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
				o.write("                - 'Players online: %ol%&%&n:&f:&f'"); //Add \n if more text is added past this point
				
				o.close();
				fw.close();
			}
			catch (IOException e)
			{
				log.severe("[LoginMessage] Error creating messages.yml file.");
			}
		}
	}
	
}
