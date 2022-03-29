package org.danny.tools.common.datetime;

import java.util.Date;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class TestDateUtil extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public TestDateUtil(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(TestDateUtil.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testGetDate() {
		int year =0, month = 0, date = 0;
		Date result = null;
		
		year = 2016;
		month = 3;
		date = 2;
		result = DateUtil.getDate(year, month, date);

		assertEquals(year, result.getYear()+1900);
		assertEquals(month, result.getMonth()+1);
		assertEquals(date, result.getDate());
	}
}
