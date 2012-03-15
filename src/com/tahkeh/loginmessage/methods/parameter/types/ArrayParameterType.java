package com.tahkeh.loginmessage.methods.parameter.types;

public class ArrayParameterType extends CreateableParameterTypes {

	public static final NativeParameterType NULL_PARAMETER_TYPE = new NativeParameterType() {

		@Override
		public String asString() {
			return null;
		}

		@Override
		public Long asLong() {
			return null;
		}

		@Override
		public Double asDouble() {
			return null;
		}

		@Override
		public Boolean asBoolean() {
			return null;
		}
	};

	private final ParameterType[] array;
	private final ParameterType value;

	public ArrayParameterType(final ParameterType[] array) {
		super("array");
		this.array = array;
		ParameterType first = null;
		if (this.array.length == 1) {
			first = this.array[0];
		}
		if (first == null) {
			this.value = NULL_PARAMETER_TYPE;
		} else {
			this.value = first;
		}
	}

	@Override
	public boolean isArray() {
		return true;
	}

	@Override
	public ParameterType[] getArray() {
		return this.array.clone();
	}

	@Override
	public Double asDouble() {
		return this.value.asDouble();
	}

	@Override
	public Long asLong() {
		return this.value.asLong();
	}

	@Override
	protected void getContent(StringBuilder builder) {
		for (int i = 0; i < this.array.length; i++) {
			if (i > 0) {
				builder.append(",");
			}
			builder.append(" \"").append(this.array[i]).append("\" ");
		}
	}

	/**
	 * Returns all values with a starting "<code>{</code>" and ending "
	 * <code>}</code>". For example if the array contains
	 * {@code foo, bar, snafu} it returns <code>{"foo", "bar", "snafu"}</code>.
	 * 
	 * @return the contents and a paar of curved brackets.
	 */
	@Override
	public String asString() {
		StringBuilder builder = new StringBuilder("{");
		this.getContent(builder);
		return builder.append("}").toString();
	}

	@Override
	public Boolean asBoolean() {
		return this.value.asBoolean();
	}
}
