package com.tahkeh.loginmessage;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class BPULoader {
	private static final Class<?>[] params = new Class[] { URL.class };
	private boolean r = true;

	public BPULoader(File file) {
		try {
			URL url = file.toURI().toURL();
			URLClassLoader cl = (URLClassLoader) ClassLoader.getSystemClassLoader();
			Method m = URLClassLoader.class.getDeclaredMethod("addURL", params);
			m.setAccessible(true);
			m.invoke(cl, new Object[] { url });
		} catch (Exception e) {
			e.printStackTrace();
			r = false;
		}
	}
	
	public boolean didLoad() {
		return r;
	}
}
