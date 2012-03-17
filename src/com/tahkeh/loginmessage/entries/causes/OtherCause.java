package com.tahkeh.loginmessage.entries.causes;

public class OtherCause extends DefaultCause {

	public OtherCause(String text) {
		super(text);
	}

	public boolean match(String cause) {
		return this.signedTextData.unsignedText.toLowerCase().equals(cause.toLowerCase());
	}

}
