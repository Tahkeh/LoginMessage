package com.tahkeh.loginmessage.matcher.entries;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.matcher.DefaultMatcher;

public abstract class DefaultEntry extends DefaultMatcher<OfflinePlayer> implements Entry {

	protected DefaultEntry(final String text) {
		super(text);
	}

	protected DefaultEntry(final SignedTextData signedTextData) {
		super(signedTextData);
	}
}
