package com.tahkeh.loginmessage.methods;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.MinecraftUtil;

public class RandomMethod extends DefaultNamedMethod {

	public RandomMethod() {
		super(true, "random", -1);
	}

	@Override
	public String call(OfflinePlayer player, String event, Parameter[] parameters, Variables globalParameters) {
		return MinecraftUtil.getRandom(parameters).parse();
	}

}
