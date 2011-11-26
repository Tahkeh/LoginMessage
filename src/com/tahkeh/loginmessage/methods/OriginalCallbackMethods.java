package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.variables.DefaultVariables;

import de.xzise.Callback;

public final class OriginalCallbackMethods {

	public static interface OriginalCallback<P extends OfflinePlayer> {
		String call(P player, String event);
	}

	public static class OriginalCallbackMethod extends OriginalMethod {

		private final OriginalCallback<OfflinePlayer> callback;

		public OriginalCallbackMethod(OriginalCallback<OfflinePlayer> callback) {
			this.callback = callback;
		}

		@Override
		protected String call(OfflinePlayer p, String event, DefaultVariables globalParameters) {
			return this.callback.call(p, event);
		}
	}

	public static class OriginalPlayerCallbackMethod extends OriginalMethod {
		private final OriginalCallback<Player> callback;

		public OriginalPlayerCallbackMethod(OriginalCallback<Player> callback) {
			this.callback = callback;
		}

		@Override
		protected String call(OfflinePlayer p, String event, DefaultVariables globalParameters) {
			if (p instanceof Player) {
				return this.callback.call((Player) p, event);
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
		public String call(P player, String event) {
			return this.callback.call(player);
		}
	}

	public static <P extends OfflinePlayer> OriginalCallback<P> createOriginalCallbackByPlayer(Callback<String, P> callback) {
		return new OriginalOfflinePlayerCallback<P>(callback);
	}

	private static final class OriginalEventCallback implements OriginalCallback<OfflinePlayer> {

		private final Callback<String, String> callback;

		public OriginalEventCallback(Callback<String, String> callback) {
			this.callback = callback;
		}

		@Override
		public String call(OfflinePlayer player, String event) {
			return this.callback.call(event);
		}
	}
	
	public static OriginalCallback<OfflinePlayer> createOriginalCallbackByEvent(Callback<String, String> callback) {
		return new OriginalEventCallback(callback);
	}
}
