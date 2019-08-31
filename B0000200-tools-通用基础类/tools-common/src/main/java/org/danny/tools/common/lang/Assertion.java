package org.danny.tools.common.lang;

import java.util.Date;
import java.util.List;

public abstract class Assertion {

	public static void isNegative(double value, String message) {
		if (!Detect.isNegative(value)) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isPositive(double value, String message) {
		if (!Detect.isPositive(value)) {
			throw new IllegalArgumentException(message);
		}
	}

	/* notEmpty */
	public static void notEmpty(String[] string, String message) {
		if (!Detect.notEmpty(string)) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void notEmpty(long[] values, String message) {
		if (!Detect.notEmpty(values)) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void notEmpty(int[] values, String message) {
		if (!Detect.notEmpty(values)) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void notEmpty(short[] values, String message) {
		if (!Detect.notEmpty(values)) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void notEmpty(List<?> list, String message) {
		if (!Detect.notEmpty(list)) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void notEmpty(String string, String message) {
		if (!Detect.notEmpty(string)) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void notEmpty(Date date, String message) {
		if (null == date) {
			throw new IllegalArgumentException(message);
		}
	}

	/* isEmpty */
	public static void isEmpty(String[] string, String message) {
		if (Detect.notEmpty(string)) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isEmpty(long[] values, String message) {
		if (Detect.notEmpty(values)) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isEmpty(List<?> list, String message) {
		if (Detect.notEmpty(list)) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isEmpty(String string, String message) {
		if (Detect.notEmpty(string)) {
			throw new IllegalArgumentException(message);
		}
	}

	/* onlyOne */
	public static void onlyOne(List<?> list, String message) {
		if (!Detect.onlyOne(list)) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static void equals(Object obj1, Object obj2, String message) {
		if (null == obj1
				&& null == obj2) {
			return;
		}
		if (null != obj1 
				&& null != obj2
				&& obj1.equals(obj2)) {
			return;
		}
		throw new IllegalArgumentException(message);
	}
	
	public static void notEquals(Object obj1, Object obj2, String message) {
		if (null == obj1
				&& null == obj2) {
			throw new IllegalArgumentException(message);
		}
		if (null != obj1 
				&& null != obj2
				&& obj1.equals(obj2)) {
			throw new IllegalArgumentException(message);
		}
	}

}
