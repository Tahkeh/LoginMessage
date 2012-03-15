package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.Bukkit;

import com.tahkeh.loginmessage.methods.parameter.types.LongParameterType;
import com.tahkeh.loginmessage.methods.preset.EmptyMethod;

public class SizeMethod extends EmptyMethod {

	public SizeMethod() {
		super("size");
	}

	@Override
	protected LongParameterType call() {
		return new LongParameterType(Bukkit.getServer().getOnlinePlayers().length);
	}

}
