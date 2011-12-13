package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.DefaultMethod;
import com.tahkeh.loginmessage.methods.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

import de.xzise.XLogger;
import de.xzise.wrappers.permissions.PermissionsHandler;

public class GroupNameMethod extends DefaultNamedMethod<BukkitVariables> {

	private final PermissionsHandler permissions;
	private final XLogger logger;

	public GroupNameMethod(final PermissionsHandler permissions, final XLogger logger) {
		super(true, "group", 0, 1);
		this.permissions = permissions;
		this.logger = logger;
	}

	@Override
	public String call(Parameter[] parameters, BukkitVariables globalParameters) {
		if (globalParameters.offlinePlayer instanceof Player && this.permissions.isActive()) {
			Integer groupIdx = null;
			if (parameters.length == 0) {
				groupIdx = 0;
			} else if (parameters.length == 1) {
				groupIdx = DefaultMethod.parseAsInteger(parameters[0].parse());
				if (groupIdx == null) {
					this.logger.warning("Invalid group index parameter.");
				}
			}
			if (groupIdx != null) {
				String[] groups = this.permissions.getGroup(((Player) globalParameters.offlinePlayer).getWorld().getName(), globalParameters.offlinePlayer.getName());
				if (groupIdx < 0 || groupIdx >= groups.length) {
					return null;
				} else {
					return groups[groupIdx];
				}
			} else {
				return null;
			}
		} else {
			return "";
		}
	}

}
