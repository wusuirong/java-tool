package org.danny.tools.toolkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * 递归按年月日生成一套目录
 * @author Administrator
 *
 */
public class 递归按年月日生成一套目录 {
	
	public static void main(String[] args) throws IOException {
		String path = null;
		if (null == args
				|| args.length < 1) {
			System.out.println("input: ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			path = br.readLine();
		} else {
			path = args[0];
		}
		path = path.replaceAll("\\\\", "/");
		
		递归按年月日生成一套目录.mkdirsFor10years(new File(path));
	}
	
	public static void mkdirsFor10years(File dir) {
		for (int year=2012; year<2021; year++) {
			for (int month=1; month<13; month++) {				
				File subDir = null;
				if (month < 10) {
					subDir = new File(dir.getAbsolutePath() + "/" + year + "-0" + month);
				} else {
					subDir = new File(dir.getAbsolutePath() + "/" + year + "-" + month);
				}
				
				for (int day=1; day<32; day++) {
					File subDir2 = null;
					if (day < 10) {
						subDir2 = new File(subDir.getAbsolutePath() + "/0" + day);
					} else {
						subDir2 = new File(subDir.getAbsolutePath() + "/" + day);
					}					 
					if (!subDir2.exists()) {
						subDir2.mkdirs();
					}
				}
				
				
			}
		}
	}

}
