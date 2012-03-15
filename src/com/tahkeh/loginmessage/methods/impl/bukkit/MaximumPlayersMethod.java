package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.Bukkit;

import com.tahkeh.loginmessage.methods.parameter.types.LongParameterType;
import com.tahkeh.loginmessage.methods.preset.EmptyMethod;

public class MaximumPlayersMethod extends EmptyMethod {

	public MaximumPlayersMethod() {
		super("max");
	}

	@Override
	protected LongParameterType call() {
		return new LongParameterType(Bukkit.getServer().getMaxPlayers());
	}

}
