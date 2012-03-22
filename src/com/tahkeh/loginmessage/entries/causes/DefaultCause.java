package com.tahkeh.loginmessage.entries.causes;

import com.tahkeh.loginmessage.matcher.DefaultMatcher;

public abstract class DefaultCause extends DefaultMatcher<String> implements Cause {

	protected DefaultCause(String text) {
		super(text);
	}
}
