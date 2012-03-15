package com.tahkeh.loginmessage.methods.preset.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.preset.OriginalCastedMethod;
import com.tahkeh.loginmessage.methods.preset.OriginalMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

import de.xzise.bukkit.util.callback.Callback;

public final class OriginalCallbackMethods {

	private OriginalCallbackMethods() {
	}

	public static interface OriginalCallback {
		ParameterType call(Player player, BukkitVariables globalParameters);
	}

	public static class OriginalCallbackMethod extends OriginalMethod<BukkitVariables> {

		private final Callback<ParameterType, BukkitVariables> callback;

		public OriginalCallbackMethod(final Callback<ParameterType, BukkitVariables> callback, final String defaultName) {
			super(defaultName);
			this.callback = callback;
		}

		@Override
		protected ParameterType call(BukkitVariables globalParameters) {
			return this.callback.call(globalParameters);
		}
	}

	public static class OriginalPlayerCallbackMethod extends OriginalCastedMethod<BukkitVariables, PlayerVariables> {
		private final OriginalCallback callback;

		public OriginalPlayerCallbackMethod(OriginalCallback callback, final String defaultName) {
			super(defaultName, PlayerVariables.class);
			this.callback = callback;
		}

		@Override
		protected ParameterType call(PlayerVariables globalParameters) {
			if (globalParameters.offlinePlayer instanceof Player) {
				return this.callback.call((Player) globalParameters.offlinePlayer, globalParameters);
			} else {
				return null;
			}
		}
	}

	private static final class OriginalOfflinePlayerCallback implements OriginalCallback {

		private final Callback<ParameterType, Player> callback;

		public OriginalOfflinePlayerCallback(Callback<ParameterType, Player> callback) {
			this.callback = callback;
		}

		@Override
		public ParameterType call(Player player, BukkitVariables globalParameters) {
			return this.callback.call(player);
		}
	}

	public static OriginalCallback createOriginalCallbackByPlayer(Callback<ParameterType, Player> callback) {
		return new OriginalOfflinePlayerCallback(callback);
	}
}
