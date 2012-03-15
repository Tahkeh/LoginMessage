package com.tahkeh.loginmessage.methods.impl;

import java.io.Reader;
import java.math.BigDecimal;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


import com.tahkeh.loginmessage.methods.Method;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.BooleanParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.DoubleParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.LongParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.MinecraftUtil;
import de.xzise.XLogger;

public class ScriptMethod implements Method<Variables> {

	private final Invocable invocable;
	private final String methodName;
	private final XLogger logger;

	public ScriptMethod(final String methodName, final Invocable invocable, final XLogger logger) {
		this.invocable = invocable;
		this.methodName = methodName;
		this.logger = logger;
	}

	public static ScriptMethod create(final String engineName, final String methodName, final Reader reader, final ScriptEngineManager engineManager, final XLogger logger) {
		ScriptEngine engine = getEngine(engineName, reader, engineManager, logger);
		if (engine instanceof Invocable) {
			return new ScriptMethod(methodName, (Invocable) engine, logger);
		} else {
			return null;
		}
	}

	public static ScriptEngine getEngine(final String name, final Reader reader, final ScriptEngineManager engineManager, final XLogger logger) {
		ScriptEngine engine = engineManager.getEngineByName(name);
		try {
			engine.eval(reader);
		} catch (ScriptException e) {
			engine = null;
			logger.warning("Unable to evaluate script.", e);
		}
		return engine;
	}

	@Override
	public ParameterType call(Parameter[] parameters, int depth, Variables globalParameters) {
		Object result = null;
		try {
			result = this.invocable.invokeFunction(this.methodName, parameters, globalParameters);
		} catch (ScriptException e) {
			this.logger.warning("Unable to call '" + this.methodName + "(Parameter[], Variables)'!", e);
		} catch (NoSuchMethodException e) {
			this.logger.warning("No such method named '" + this.methodName + "(Parameter[], Variables)'!", e);
		}
		// Test the result for the different types
		if (result instanceof Number) {
			Number number = (Number) result;
			if (number instanceof Double || number instanceof Float || number instanceof BigDecimal) {
				return new DoubleParameterType(number.doubleValue(), MinecraftUtil.MAX_TWO_DECIMALS_FORMAT);
			} else {
				return new LongParameterType(number.longValue());
			}
		} else if (result instanceof Boolean) {
			return new BooleanParameterType((Boolean) result);
		} else if (result != null) {
			return new StringParameterType(result.toString());
		} else {
			return null;
		}
	}

}
