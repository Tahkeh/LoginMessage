package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.DefaultCastedNamedMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

import de.xzise.XLogger;
import de.xzise.wrappers.permissions.PermissionsHandler;

public class GroupNameMethod extends DefaultCastedNamedMethod<BukkitVariables, PlayerVariables> {

	private final PermissionsHandler permissions;
	private final XLogger logger;

	public GroupNameMethod(final PermissionsHandler permissions, final XLogger logger) {
		super("group", PlayerVariables.class, 0, 1);
		this.permissions = permissions;
		this.logger = logger;
	}

	@Override
	public ParameterType innerCall(Parameter[] parameters, PlayerVariables globalParameters) {
		if (globalParameters.offlinePlayer instanceof Player && this.permissions.isActive()) {
			Long groupIdx = null;
			if (parameters.length == 0) {
				groupIdx = 0L;
			} else if (parameters.length == 1) {
				groupIdx = parameters[0].parse().asLong();
				if (groupIdx == null) {
					this.logger.warning("Invalid group index parameter.");
				}
			}
			if (groupIdx != null) {
				String[] groups = this.permissions.getGroup(((Player) globalParameters.offlinePlayer).getWorld().getName(), globalParameters.offlinePlayer.getName());
				if (groupIdx < 0 || groupIdx >= groups.length) {
					return null;
				} else {
					return new StringParameterType(groups[groupIdx.intValue()]);
				}
			} else {
				return null;
			}
		} else {
			return StringParameterType.EMPTY_PARAMETER_TYPE;
		}
	}

}
