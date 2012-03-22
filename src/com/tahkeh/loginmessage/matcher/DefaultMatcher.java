package com.tahkeh.loginmessage.matcher;

import java.util.Collection;

public abstract class DefaultMatcher<T> implements Matcher<T> {

	protected final SignedTextData signedTextData;

	protected DefaultMatcher(String text) {
		this(new SignedTextData(text));
	}

	protected DefaultMatcher(SignedTextData signedTextData) {
		this.signedTextData = signedTextData;
	}

	@Override
	public boolean isPositive() {
		return this.signedTextData.positive;
	}

	public static final class SignedTextData {
		public final boolean positive;
		public final boolean signed;
		public final String unsignedText;

		public SignedTextData(final String signedText) {
			if (signedText.length() > 0) {
				final char first = signedText.charAt(0);
				this.positive = first != '-';
				this.signed = !this.positive || first == '+';
				this.unsignedText = !this.signed ? signedText : signedText.substring(1);
			} else {
				this.positive = true;
				this.signed = false;
				this.unsignedText = "";
			}
		}

		public SignedTextData(final boolean positive, final boolean signed, final String unsignedText) {
			this.positive = positive;
			this.signed = signed;
			this.unsignedText = unsignedText;
		}
	}

	public static <T> boolean match(T parameter, Collection<? extends Matcher<? super T>> matchers) {
		boolean match = false;
		for (Matcher<? super T> matcher : matchers) {
			if (matcher.match(parameter)) {
				if (!matcher.isPositive()) {
					return false;
				} else {
					match = true;
				}
			}
		}
		return match;
	}

	public static boolean isPositive(String text) {
		return text.length() == 0 || text.charAt(0) != '-';
	}

	public static boolean isSigned(String text) {
		return !isPositive(text) || (text.length() > 0 && text.charAt(0) == '+');
	}

	public static String getUnsignedText(String text) {
		return !isSigned(text) ? text : text.substring(1);
	}
}
