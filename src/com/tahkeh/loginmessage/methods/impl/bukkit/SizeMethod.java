package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.Bukkit;

import com.tahkeh.loginmessage.methods.EmptyMethod;

public class SizeMethod extends EmptyMethod {

	public SizeMethod() {
		super("size");
	}

	@Override
	protected String call() {
		return Integer.toString(Bukkit.getServer().getOnlinePlayers().length);
	}

}
