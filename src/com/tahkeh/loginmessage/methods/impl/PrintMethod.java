package com.tahkeh.loginmessage.methods.impl;

import java.util.Iterator;

import com.tahkeh.loginmessage.methods.MethodParser;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;
import com.tahkeh.loginmessage.methods.parameter.types.StringParameterType;
import com.tahkeh.loginmessage.methods.preset.DefaultNamedMethod;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.collections.ArrayIterator;

/**
 * Returns all parameters.
 */
public class PrintMethod<V extends Variables> extends DefaultNamedMethod<V> {

	private final boolean isRecursive;
	private final MethodParser<V> parser;

	public PrintMethod(final boolean isRecursive, final String defaultName, final MethodParser<V> parser) {
		super(defaultName, -1);
		this.isRecursive = isRecursive;
		this.parser = parser;
	}

	public static <V extends Variables> PrintMethod<V> create(final boolean isRecursive, final String defaultName, final MethodParser<V> parser) {
		return new PrintMethod<V>(isRecursive, defaultName, parser);
	}

	@Override
	public ParameterType call(Parameter[] parameters, int depth, V globalParameters) {
		StringBuilder builder = new StringBuilder();
		final ParameterType[] typeArray = new ParameterType[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			if (this.isRecursive) {
				typeArray[i] = parameters[i].parse();
			} else {
				typeArray[i] = new StringParameterType(parameters[i].getText());
			}
		}
		printArray(typeArray, builder);
		if (this.isRecursive) {
			return this.parser.parseLine(builder.toString(), globalParameters, depth + 1);
		} else {
			return new StringParameterType(builder.toString());
		}
	}

	private void printArray(final ParameterType[] array, final StringBuilder builder) {
		for (Iterator<ParameterType> parameterItr = new ArrayIterator<ParameterType>(array); parameterItr.hasNext();) {
			final ParameterType type = parameterItr.next();
			if (type != null && type.isArray()) {
				this.printArray(type.getArray(), builder);
			} else {
				final String next = type == null ? null : type.asString();
				if (next == null) {
					builder.append("##null##");
				} else if (next.isEmpty()) {
					builder.append("##empty##");
				} else {
					builder.append(next);
				}
				if (parameterItr.hasNext()) {
					builder.append(" ");
				}
			}
		}
	}

	public PrintMethod<V> register() {
		super.register(this.parser);
		return this;
	}

	public PrintMethod<V> unregister() {
		super.unregister(this.parser);
		return this;
	}
}
