package com.tahkeh.loginmessage.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import de.xzise.XLogger;

public class PropertiesFile {
	private final File file;
	private Properties props = new Properties();

	public PropertiesFile(File file, XLogger logger) {
		this.file = file;

		try {
			if (file.exists())
				load();
			else
				save();
		} catch (IOException ex) {
			logger.severe("Error creating " + file.getAbsolutePath() + " file.");
		}
	}

	public void load() throws IOException {
		this.props.load(new FileInputStream(this.file));
	}

	public void save() {
		try {
			this.props.store(new FileOutputStream(this.file), null);
		} catch (IOException localIOException) {
		}
	}

	public boolean containsKey(String var) {
		return this.props.containsKey(var);
	}

	public String getProperty(String var) {
		return this.props.getProperty(var);
	}

	public void removeKey(String var) {
		if (this.props.containsKey(var)) {
			this.props.remove(var);
			save();
		}
	}

	public boolean keyExists(String key) {
		return containsKey(key);
	}

	public String getString(String key) {
		if (containsKey(key)) {
			return getProperty(key);
		}

		return "";
	}

	public String getString(String key, String value) {
		if (containsKey(key)) {
			return getProperty(key);
		}

		setString(key, value);
		return value;
	}

	public void setString(String key, String value) {
		this.props.put(key, value);
		save();
	}

	public int getInt(String key) {
		if (containsKey(key)) {
			return Integer.parseInt(getProperty(key));
		}

		return 0;
	}

	public int getInt(String key, int value) {
		if (containsKey(key)) {
			return Integer.parseInt(getProperty(key));
		}

		setInt(key, value);
		return value;
	}

	public void setInt(String key, int value) {
		this.props.put(key, String.valueOf(value));

		save();
	}

	public double getDouble(String key) {
		if (containsKey(key)) {
			return Double.parseDouble(getProperty(key));
		}

		return 0.0D;
	}

	public double getDouble(String key, double value) {
		if (containsKey(key)) {
			return Double.parseDouble(getProperty(key));
		}

		setDouble(key, value);
		return value;
	}

	public void setDouble(String key, double value) {
		this.props.put(key, String.valueOf(value));

		save();
	}

	public long getLong(String key) {
		if (containsKey(key)) {
			return Long.parseLong(getProperty(key));
		}

		return 0L;
	}

	public long getLong(String key, long value) {
		if (containsKey(key)) {
			return Long.parseLong(getProperty(key));
		}

		setLong(key, value);
		return value;
	}

	public void setLong(String key, long value) {
		this.props.put(key, String.valueOf(value));

		save();
	}

	public boolean getBoolean(String key) {
		if (containsKey(key)) {
			return Boolean.parseBoolean(getProperty(key));
		}

		return false;
	}

	public boolean getBoolean(String key, boolean value) {
		if (containsKey(key)) {
			return Boolean.parseBoolean(getProperty(key));
		}

		setBoolean(key, value);
		return value;
	}

	public void setBoolean(String key, boolean value) {
		this.props.put(key, String.valueOf(value));

		save();
	}
}