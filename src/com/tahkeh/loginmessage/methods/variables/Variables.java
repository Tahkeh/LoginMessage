package com.tahkeh.loginmessage.methods.variables;

public class Variables {

	public final boolean leaveEvent;
	public final String name;

	// Default Variables as some kind of singletons
	public final static Variables INSTANCE_QUIT = new Variables(true, "quit");
	public final static Variables INSTANCE_FIRST_LOGIN = new Variables(false, "firstlogin");
	public final static Variables INSTANCE_LOGIN = new Variables(false, "login");
	public final static Variables INSTANCE_INTERVAL = new Variables(false, "interval");
	public final static Variables INSTANCE_LIST = new Variables(false, "list");

	protected Variables(final boolean leaveEvent, final String name) {
		this.leaveEvent = leaveEvent;
		this.name = name;
	}
}
