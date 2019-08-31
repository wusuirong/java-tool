package org.danny.tools.common.datetime;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * @param year
	 * @param month 1-12
	 * @param date 1-31
	 * @return
	 */
	public static Date getDate(int year, int month, int date) {
		if (month < 1 || month > 12) {
			throw new IllegalArgumentException("month must between 1 to 12");
		}
		if (date < 1 || date > 31) {
			throw new IllegalArgumentException("date must between 1 to 31");
		}
		Calendar c = Calendar.getInstance();
		c.set(year, month-1, date, 0, 0, 0);
		Date d = c.getTime();
		return d;
	}
	
	public static int getYearOfDate(Date date) {
		if (null == date) {
			throw new IllegalArgumentException("date is null");
		}
		return date.getYear()+1900;
	}
	
	public static int getMonthOfDate(Date date) {
		if (null == date) {
			throw new IllegalArgumentException("date is null");
		}
		return date.getMonth()+1;
	}
}
