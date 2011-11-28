package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.DefaultVariables;

import de.xzise.MinecraftUtil;

public class RandomMethod extends DefaultMethod {

	public RandomMethod() {
		super(true, -1);
	}

	@Override
	public String call(OfflinePlayer player, String event, Parameter[] parameters, DefaultVariables globalParameters) {
		return MinecraftUtil.getRandom(parameters).parse();
	}

}
