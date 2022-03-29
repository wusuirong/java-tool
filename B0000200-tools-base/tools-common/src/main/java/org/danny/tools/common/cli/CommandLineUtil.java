package org.danny.tools.common.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class CommandLineUtil {
	private static final int BUF_SIZE = 4096;
	
	/**
	 * 暂时不支持中文
	 * @return
	 * @throws IOException
	 */
	public static String readLine() throws IOException {
		int[] buf = new int[BUF_SIZE];
		int i = 0;
		int c = 0;
		//把开头的回车，换行符过滤掉
		while (-1 != (c = System.in.read())) {
			if ('\r' == c || '\n' == c) {
				continue;
			} else {
				buf[i++] = c;
				break;
			}
		}
		while (-1 != (c = System.in.read())) {
			if ('\r' == c || '\n' == c) {
				break;
			}
			buf[i++] = c;
		}
		String s = new String(buf, 0, i);

		return s;
	}
}
