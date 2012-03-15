package com.tahkeh.loginmessage.methods.impl.bukkit;

import org.bukkit.entity.Player;

import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.bukkit.OriginalPlayerMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

public class DisplayNameMethod extends OriginalPlayerMethod {

	public DisplayNameMethod() {
		super("dpnm");
	}

	@Override
	protected StringParameterType call(Player p, PlayerVariables globalParameters) {
		return new StringParameterType(p.getDisplayName());
	}

}
