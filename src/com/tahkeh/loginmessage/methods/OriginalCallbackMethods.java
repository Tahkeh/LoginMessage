package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.bukkit.util.callback.Callback;

public final class OriginalCallbackMethods {

	public static interface OriginalCallback<P extends OfflinePlayer> {
		String call(P player, Variables globalParameters);
	}

	public static class OriginalCallbackMethod extends OriginalMethod {

		private final OriginalCallback<OfflinePlayer> callback;

		public OriginalCallbackMethod(final OriginalCallback<OfflinePlayer> callback, final String defaultName) {
			super(defaultName);
			this.callback = callback;
		}

		@Override
		protected String call(OfflinePlayer p, Variables globalParameters) {
			return this.callback.call(p, globalParameters);
		}
	}

	public static class OriginalPlayerCallbackMethod extends OriginalMethod {
		private final OriginalCallback<Player> callback;

		public OriginalPlayerCallbackMethod(OriginalCallback<Player> callback, final String defaultName) {
			super(defaultName);
			this.callback = callback;
		}

		@Override
		protected String call(OfflinePlayer p, Variables globalParameters) {
			if (p instanceof Player) {
				return this.callback.call((Player) p, globalParameters);
			} else {
				return null;
			}
		}
	}

	private OriginalCallbackMethods() {
	}

	private static final class OriginalOfflinePlayerCallback<P extends OfflinePlayer> implements OriginalCallback<P> {

		private final Callback<String, P> callback;

		public OriginalOfflinePlayerCallback(Callback<String, P> callback) {
			this.callback = callback;
		}

		@Override
		public String call(P player, Variables globalParameters) {
			return this.callback.call(player);
		}
	}

	public static <P extends OfflinePlayer> OriginalCallback<P> createOriginalCallbackByPlayer(Callback<String, P> callback) {
		return new OriginalOfflinePlayerCallback<P>(callback);
	}

	private static final class OriginalGlobalParametersCallback implements OriginalCallback<OfflinePlayer> {

		private final Callback<String, Variables> callback;

		public OriginalGlobalParametersCallback(Callback<String, Variables> callback) {
			this.callback = callback;
		}

		@Override
		public String call(OfflinePlayer player, Variables globalParameters) {
			return this.callback.call(globalParameters);
		}
	}
	
	public static OriginalCallback<OfflinePlayer> createOriginalCallbackByEvent(Callback<String, Variables> callback) {
		return new OriginalGlobalParametersCallback(callback);
	}
}
