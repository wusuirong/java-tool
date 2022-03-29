package org.danny.tools.toolkit;
import java.io.File;


public class 文件查找工具 {

	public static void printBigFile(String path, int minimumSize) {
		File f = new File(path);
		if (f.isDirectory()) {
			printFileSize(f, minimumSize);
		}
	}
	
	private static void printFileSize(File dir, int minimumSize) {
		if (!dir.exists()) {
			return;
		}
		
		File[] files = dir.listFiles();
		for (int i=0; i<files.length; i++) {
			File file = files[i];
			if (file.isFile()) {
				if (minimumSize < file.length()) {
					System.out.println(file.length()/1024/1024 + "M" + ":" + file.getAbsolutePath());
				}				
			} else if (file.isDirectory()) {
				if (minimumSize < file.length()) {
					System.out.println(file.length()/1024/1024 + "M" + ":" + file.getAbsolutePath());
				}
				printFileSize(file, minimumSize);
			}
		}
	}
	
	public static void main(String[] args) {
		文件查找工具.printBigFile("J:/", 1024*1024);
	}

}
