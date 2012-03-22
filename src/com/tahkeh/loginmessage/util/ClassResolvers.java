package com.tahkeh.loginmessage.util;

import de.xzise.XLogger;

public class ClassResolvers {

	private ClassResolvers() {}

	public static final String CRAFTBUKKIT_ENTITY_PREFIX = "org.bukkit.craftbukkit.entity.";
	public static final String BUKKIT_ENTITY_PREFIX = "org.bukkit.entity.";

	public static interface ClassResolver {
		boolean isSuperClass(final Class<?> extendingClass);
	}

	public static final ClassResolver ALL_CLASS_RESOLVER = new ClassResolver() {
		@Override
		public boolean isSuperClass(Class<?> extendingClass) {
			return true;
		}
	};

	public static final class ConstantClassResolver implements ClassResolver {
		private final Class<?> classObject;

		public ConstantClassResolver(final Class<?> classObject) {
			this.classObject = classObject;
		}

		public static ConstantClassResolver create(final String className, final XLogger logger) {
			Class<?> classObject = null;
			try {
				classObject = Class.forName(className);
			} catch (ClassNotFoundException e) {
				logger.severe("Class '" + className + "' not found!", e);
			}
			if (classObject != null) {
				return new ConstantClassResolver(classObject);
			} else {
				return null;
			}
		}

		@Override
		public boolean isSuperClass(Class<?> extendingClass) {
			return this.classObject.isAssignableFrom(extendingClass);
		}
	}

	public static final class LazyClassResolver implements ClassResolver {
		private final XLogger logger;
		private final String className;
		private Class<?> classObject = null;

		public LazyClassResolver(final String className, final XLogger logger) {
			this.className = className;
			this.logger = logger;
		}

		public Class<?> getResolvedClass() {
			if (this.classObject == null) {
				try {
					this.classObject = Class.forName(this.className);
				} catch (ClassNotFoundException e) {
					this.logger.severe("Class '" + this.className + "' not found!", e);
				}
			}
			return classObject;
		}

		@Override
		public boolean isSuperClass(final Class<?> extendingClass) {
			final Class<?> superClass = getResolvedClass();
			if (superClass != null && extendingClass != null) {
				return superClass.isAssignableFrom(extendingClass);
			} else {
				return false;
			}
		}
	}

	public static ClassResolver parseClassName(final String className, final XLogger logger, final boolean lazy) {
		if (className.equals("*")) {
			return ALL_CLASS_RESOLVER;
		} else {
			final String expandedPrefix;
			if (className.startsWith("B:")) {
				expandedPrefix = BUKKIT_ENTITY_PREFIX;
			} else if (className.startsWith("C:")) {
				expandedPrefix = CRAFTBUKKIT_ENTITY_PREFIX;
			} else {
				expandedPrefix = "";
			}
			if (lazy) {
				return new LazyClassResolver(expandedPrefix + className, logger);
			} else {
				return ConstantClassResolver.create(expandedPrefix + className, logger);
			}
		}
	}
}
