package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.bukkit.OriginalPlayerMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

import de.xzise.bukkit.util.callback.CallbackTriple;
import de.xzise.wrappers.permissions.Permission;
import de.xzise.wrappers.permissions.PermissionsHandler;

public class PermissionMethod<T extends Object> extends OriginalPlayerMethod {

	private final PermissionsHandler handler;
	private final Permission<T> permission;
	private final CallbackTriple<String, PermissionsHandler, Permission<T>, Player> valueCallback;

	public static final PlayerStringCallback PLAYER_STRING_CALLBACK = new PlayerStringCallback();
	public static class PlayerStringCallback implements CallbackTriple<String, PermissionsHandler, Permission<String>, Player> {
		@Override
		public String call(PermissionsHandler handler, Permission<String> permission, Player player) {
			return handler.getString(player, permission);
		}
	}

	public PermissionMethod(final PermissionsHandler handler, final Permission<T> permission, final CallbackTriple<String, PermissionsHandler, Permission<T>, Player> valueCallback, final String defaultName) {
		super(defaultName);
		this.handler = handler;
		this.permission = permission;
		this.valueCallback = valueCallback;
	}

	@Override
	protected final StringParameterType call(Player player, PlayerVariables globalParameters) {
		if (this.handler.isActive()) {
			String str = this.valueCallback.call(this.handler, this.permission, player);
			if (str == null) {
				return StringParameterType.EMPTY_PARAMETER_TYPE;
			} else {
				return new StringParameterType(str);
			}
		} else {
			return StringParameterType.EMPTY_PARAMETER_TYPE;
		}
	}
}
