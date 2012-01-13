package com.tahkeh.loginmessage.entries.causes;

public abstract class DefaultCause implements Cause {

	private final boolean positive;
    protected final String value;
    
    protected DefaultCause(String text) {
        this.positive = isPositive(text); 
        this.value = getUnsignedText(text);
    }

    @Override
    public boolean isPositive() {
        return this.positive;
    }

    public static boolean isPositive(String text) {
        return text.charAt(0) != '-';
    }
    
    public static boolean isSigned(String text) {
        return !isPositive(text) || text.charAt(0) == '+';
    }
    
    public static String getUnsignedText(String text) {
        return !isSigned(text) ? text : text.substring(1);
    }
}
