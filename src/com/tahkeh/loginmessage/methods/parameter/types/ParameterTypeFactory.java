package com.tahkeh.loginmessage.methods.parameter.types;

import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

public interface ParameterTypeFactory {

	ParameterType create(final Parameter[] parameters, final Variables variables);
}
