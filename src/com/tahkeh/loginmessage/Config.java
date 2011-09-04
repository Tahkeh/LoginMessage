package com.tahkeh.loginmessage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Config //For all your configuration needs
{

	private File folder;

	public Config(File folder, Main instance)
	{
		this.folder = folder;
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
				o.write("#useeco: If false, iConomy/BOSEconomy will not be used, even if it's in the plugins folder.\n");
				o.write("useeco: true\n");
				o.write("\n");
				o.write("#useper: If false, Permissions will not be used, even if it's in the plugins folder.\n");
				o.write("useper: true\n");
				o.write("\n");
				o.write("#format: This is the formatting used for the %srtime code.\n");
				o.write("#Go to goo.gl/3nZ5y to learn how to properly format.\n");
				o.write("format: K:mm a z\n");
				o.write("\n");
				o.write("#These fields are what will be displayed if a GeoIP lookup fails.\n");
				o.write("cityfail: Ragington\n");
				o.write("ccodefail: USL\n");
				o.write("cnamefail: United States of Lolz\n");
				o.write("zipfail: 09001\n");
				o.write("rcodefail: TA\n");
				o.write("rnamefail: Trollida\n");
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
				System.out.println("[LoginMessage] Error creating config.yml file.");
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
				System.out.println("[LoginMessage] Error creating messages.yml file.");
			}
		}
	}
	
}
