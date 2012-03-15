package com.tahkeh.loginmessage.methods.parameter;

import com.tahkeh.loginmessage.methods.parameter.types.ParameterType;

public interface Parameter {

	ParameterType parse();

	String getText();
}
