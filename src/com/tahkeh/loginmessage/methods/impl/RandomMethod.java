package com.tahkeh.loginmessage.methods.impl;

import com.tahkeh.loginmessage.methods.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.MinecraftUtil;

public class RandomMethod extends DefaultNamedMethod<Variables> {

	public RandomMethod() {
		super(true, "random", -1);
	}

	@Override
	public String call(Parameter[] parameters, Variables globalParameters) {
		return MinecraftUtil.getRandom(parameters).parse();
	}

}
