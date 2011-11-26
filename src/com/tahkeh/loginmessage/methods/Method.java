package com.tahkeh.loginmessage.methods;


import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.variables.DefaultVariables;

public interface Method {

	/**
	 * Executes the method and returns null if the parameters are invalid.
	 * @param parameters the parameters.
	 * @return the parsed string or null if invalid.
	 */
	String call(OfflinePlayer player, String event, String[] parameters, DefaultVariables globalParameters);

	/**
	 * Defines if this method has recursive calls.
	 * @return if this method has recursive calls.
	 */
	boolean recursive();
}
