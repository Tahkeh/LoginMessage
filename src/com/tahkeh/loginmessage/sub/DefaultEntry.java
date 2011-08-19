package com.tahkeh.loginmessage.sub;

public abstract class DefaultEntry implements Entry {

    private final boolean positive;
    protected final String value;
    
    protected DefaultEntry(String text) {
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
    
    public static String getUnsignedText(String text) {
        return isPositive(text) || text.charAt(0) != '+' ? text : text.substring(1);
    }
}
