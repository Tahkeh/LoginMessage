package com.tahkeh.loginmessage.entries.causes;

public interface Cause {
	boolean isPositive();
	boolean match(String cause);
}
