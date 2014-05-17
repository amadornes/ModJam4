package com.amadornes.tbircme.util;

import java.lang.reflect.Field;

public class ReflectionUtils {

	public static final Object get(Object o, String field) {
		Field f = null;
		;
		try {
			f = o.getClass().getDeclaredField(field);
			if (f == null)
				return null;

			boolean accessible = f.isAccessible();
			if (!accessible)
				f.setAccessible(true);

			Object obj = f.get(o);

			f.setAccessible(accessible);

			return obj;
		} catch (Exception e) {
		}

		return null;
	}

}
