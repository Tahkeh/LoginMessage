package com.tahkeh.loginmessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import de.xzise.XLogger;

public class FileConfigurationPair<C extends FileConfiguration> {

	public final File file;
	public final C fileConfiguration;
	public final String name;
	private final XLogger logger;

	public FileConfigurationPair(final File file, final C fileConfiguration, final String name, final XLogger logger) {
		this.file = file;
		this.fileConfiguration = fileConfiguration;
		this.name = name;
		this.logger = logger;
	}

	public FileConfigurationPair<C> load() {
		FileConfigurationPair.load(this.file, this.fileConfiguration, this.name, this.logger);
		return this;
	}

	public static <C extends FileConfiguration> C load(final File file, final C fileConfiguration, final String name, final XLogger logger) {
		try {
			fileConfiguration.load(file);
		} catch (FileNotFoundException e) {
			logger.severe("Unable to load " + name + " file, because it wasn't found.");
		} catch (IOException e) {
			logger.severe("Unable to load " + name + " file.", e);
		} catch (InvalidConfigurationException e) {
			logger.severe("Tried to load an invalid " + name + " file.", e);
		}
		return fileConfiguration;
	}
}
