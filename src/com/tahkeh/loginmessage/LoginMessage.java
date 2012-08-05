package com.tahkeh.loginmessage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class LoginMessage extends JavaPlugin {
	public final static String BPU = "BukkitPluginUtilities";
	public final static String BPU_NAME = "bukkitutil-1.2.3.jar";
	public final static String BPU_PATH = "http://cloud.github.com/downloads/xZise/Bukkit-Plugin-Utilties/" + BPU_NAME;
	public final static String BPU_DEST = "lib" + File.separator + BPU + ".jar";
	
	public Main main;

	@Override
	public void onDisable() {
		this.getServer().getScheduler().cancelTasks(this);
		main.onDisable();
	}

	@Override
	public void onEnable() {
		File folder = getDataFolder();
		folder.mkdir();
		if (downloadBPU(BPU_PATH, BPU_DEST, BPU)) {
			main = new Main(this);
			main.onEnable();
		} else {
			Logger.getLogger("Minecraft").severe("[LoginMessage] Unable to install '" + BPU + "'! Disabling plugin.");
			this.getPluginLoader().disablePlugin(this);
		}
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return main.onCommand(sender, command, label, args);
	}
	
	public static boolean downloadBPU(String urlpath, String dest, String file) {
		File destFile = new File(dest).getAbsoluteFile();
		boolean r = downloadFile(urlpath, dest, file);
		if (r) {
			BPULoader bpuloader = new BPULoader(destFile);
			r = bpuloader.didLoad();
		}
		return r;
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
					log.info("[LoginMessage] Successfully downloaded " + file + " (" + getBinaryPrefixValue(count) + "B)!");
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
}
