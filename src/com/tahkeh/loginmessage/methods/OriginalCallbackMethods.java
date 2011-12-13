package com.tahkeh.loginmessage.methods;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;

import de.xzise.bukkit.util.callback.Callback;

public final class OriginalCallbackMethods {

	private OriginalCallbackMethods() {
	}

	public static interface OriginalCallback {
		String call(Player player, BukkitVariables globalParameters);
	}

	public static class OriginalCallbackMethod extends OriginalMethod<BukkitVariables> {

		private final Callback<String, BukkitVariables> callback;

		public OriginalCallbackMethod(final Callback<String, BukkitVariables> callback, final String defaultName) {
			super(defaultName);
			this.callback = callback;
		}

		@Override
		protected String call(BukkitVariables globalParameters) {
			return this.callback.call(globalParameters);
		}
	}

	public static class OriginalPlayerCallbackMethod extends OriginalMethod<BukkitVariables> {
		private final OriginalCallback callback;

		public OriginalPlayerCallbackMethod(OriginalCallback callback, final String defaultName) {
			super(defaultName);
			this.callback = callback;
		}

		@Override
		protected String call(BukkitVariables globalParameters) {
			if (globalParameters.offlinePlayer instanceof Player) {
				return this.callback.call((Player) globalParameters.offlinePlayer, globalParameters);
			} else {
				return null;
			}
		}
	}

	private static final class OriginalOfflinePlayerCallback implements OriginalCallback {

		private final Callback<String, Player> callback;

		public OriginalOfflinePlayerCallback(Callback<String, Player> callback) {
			this.callback = callback;
		}

		@Override
		public String call(Player player, BukkitVariables globalParameters) {
			return this.callback.call(player);
		}
	}

	public static OriginalCallback createOriginalCallbackByPlayer(Callback<String, Player> callback) {
		return new OriginalOfflinePlayerCallback(callback);
	}
}
