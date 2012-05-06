package com.tahkeh.loginmessage.matcher;

/**
 * Basic matcher interface to check if the parameter is matched by this
 * implementation. Any matcher could be positive or negative. If the matcher is
 * matching the parameter and is positive, than the parameter is meant by the
 * matcher. If the matcher is matching and negative, than it wasn't meant by the
 * matcher and should be skipped (no other matching positive node override a
 * matching negative one).
 * 
 * @author Tahkeh
 */
public interface Matcher<T> {
	/**
	 * If the matcher is positive, matching parameters are meant otherwise the matching parameters are never meant.
	 * 
	 * @return If this entry is positive.
	 */
	boolean isPositive();

	/**
	 * Returns if the parameter matches the matcher.
	 * 
	 * @param parameter
	 *            the parameter to test.
	 * @return if the parameter matches the matcher.
	 */
	boolean match(T parameter);
}