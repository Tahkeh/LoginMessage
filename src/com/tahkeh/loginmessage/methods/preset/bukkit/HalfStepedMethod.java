package com.tahkeh.loginmessage.methods.preset.bukkit;

import java.text.DecimalFormat;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.DoubleParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.LongParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.preset.DefaultCastedNamedMethod;
import com.tahkeh.loginmessage.methods.variables.bukkit.BukkitVariables;
import com.tahkeh.loginmessage.methods.variables.bukkit.PlayerVariables;

import de.xzise.MinecraftUtil;

public abstract class HalfStepedMethod extends DefaultCastedNamedMethod<BukkitVariables, PlayerVariables> {

	private static final DecimalFormat FORMAT = MinecraftUtil.getFormatWithMinimumDecimals(1, 1);

	public HalfStepedMethod(String defaultName) {
		super(defaultName, PlayerVariables.class, 0, 1);
	}

	protected abstract Integer getInteger(PlayerVariables globalParameters);

	@Override
	public ParameterType innerCall(Parameter[] parameters, PlayerVariables globalParameters) {
		final Boolean halfsteps;
		if (parameters.length == 0) {
			halfsteps = true;
		} else if (parameters.length == 1) {
			halfsteps = parameters[0].parse().asBoolean();
		} else {
			halfsteps = null;
		}
		if (halfsteps != null) {
			final Integer value = getInteger(globalParameters);
			if (value != null) {
				if (halfsteps) {
					return new DoubleParameterType(((double) value) / 2, FORMAT);
				} else {
					return new LongParameterType(value);
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
