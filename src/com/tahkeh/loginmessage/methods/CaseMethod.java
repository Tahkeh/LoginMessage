package com.tahkeh.loginmessage.methods;

import java.util.Map;

import org.bukkit.OfflinePlayer;

import com.tahkeh.loginmessage.Message;
import com.tahkeh.loginmessage.methods.parameter.Parameter;
import com.tahkeh.loginmessage.methods.variables.Variables;

import de.xzise.MinecraftUtil;
import de.xzise.bukkit.util.callback.Callback;
import de.xzise.collections.ArrayReferenceList;

public abstract class CaseMethod extends DefaultNamedMethod {

	public static enum CaseEnum {
		LOWER_CASE("lower"),
		UPPER_CASE("upper"),
		CAMEL_CASE("camel"),
		FIRST_UPPER("first"),
		CUSTOM("custom"),
		NONE("none");

		public final String name;

		private CaseEnum(final String name) {
			this.name = name;
		}

		public static final Map<String, CaseEnum> CASE_ENUMS = MinecraftUtil.createReverseMutableEnumMap(CaseEnum.class, new Callback<String, CaseEnum>() {
			@Override
			public String call(CaseEnum caseEnum) {
			    return caseEnum.name.toLowerCase();
			}
		});
	}

	public CaseMethod(final String defaultName) {
		super(true, defaultName, 0, 1, 3);
	}

	@Override
	public String call(OfflinePlayer player, Parameter[] parameters, Variables globalParameters) {
		CaseEnum caseEnum = null;
		if (parameters.length == 0) {
			caseEnum = CaseEnum.NONE;
		} else if (parameters.length == 1 || parameters.length == 3) {
			caseEnum = CaseEnum.CASE_ENUMS.get(parameters[0].parse().toLowerCase());
		}
		if ((caseEnum == CaseEnum.CUSTOM && parameters.length == 3) || (caseEnum != CaseEnum.CUSTOM && caseEnum != null && parameters.length == 1)) {
			final String result = this.call(player, globalParameters);
			switch (caseEnum) {
			case CUSTOM :
				final char[] upperTrigger = new String(parameters[1].parse()).toCharArray();
				final char[] upperReceiver = new String(parameters[2].parse()).toCharArray();
				final char[] processedCustom = new char[result.length()];
				int indexCustom = 0;
				boolean makeUpperCustom = true;
				for (char c : result.toCharArray()) {
					if (ArrayReferenceList.contains(c, upperTrigger)) {
						makeUpperCustom = true;
					} else if (makeUpperCustom && ArrayReferenceList.contains(c, upperReceiver)) {
						c = Character.toUpperCase(c);
						makeUpperCustom = false;
					}
					processedCustom[indexCustom++] = c;
				}
				return new String(processedCustom);
			case CAMEL_CASE :
				final char[] processed = new char[result.length()];
				int index = 0;
				boolean makeUpper = true;
				for (char c : result.toCharArray()) {
					if (Character.isWhitespace(c)) {
						makeUpper = true;
					} else if (makeUpper && Character.isLetter(c)) {
						c = Character.toUpperCase(c);
						makeUpperCustom = false;
					}
					processed[index++] = c;
				}
				return new String(processed);
			case FIRST_UPPER :
				return Message.toCapitalCase(result);
			case UPPER_CASE :
				return result.toUpperCase();
			case LOWER_CASE :
				return result.toLowerCase();
			case NONE :
				return result;
			default :
				return null;
			}
		} else {
			return null;
		}
	}

	protected abstract String call(OfflinePlayer player, Variables globalParameters);
}
