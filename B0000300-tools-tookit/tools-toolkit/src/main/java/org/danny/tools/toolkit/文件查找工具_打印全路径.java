package org.danny.tools.toolkit;
import org.danny.tools.tmp.Base;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class 文件查找工具_打印全路径 {
	//__ Videos and Training Materials
	//CMS_Integration
	//Demo_Videos
	//Others
	//Personal
	//Ra
	//Shared
	//Training
	//Zerg
	public static void main(String[] args) {
		文件查找工具_打印全路径.start("Z:/CMS_Integration", new PrintFilePath("C:\\tempDoc\\share_data\\CMS_Integration.txt"));
		文件查找工具_打印全路径.start("Z:/Demo_Videos", new PrintFilePath("C:\\tempDoc\\share_data\\Demo_Videos.txt"));
		文件查找工具_打印全路径.start("Z:/Others", new PrintFilePath("C:\\tempDoc\\share_data\\Others.txt"));
		文件查找工具_打印全路径.start("Z:/Personal", new PrintFilePath("C:\\tempDoc\\share_data\\Personal.txt"));
		文件查找工具_打印全路径.start("Z:/Ra", new PrintFilePath("C:\\tempDoc\\share_data\\Ra.txt"));
		文件查找工具_打印全路径.start("Z:/Shared", new PrintFilePath("C:\\tempDoc\\share_data\\Shared.txt"));
		文件查找工具_打印全路径.start("Z:/Training", new PrintFilePath("C:\\tempDoc\\share_data\\Training.txt"));
		文件查找工具_打印全路径.start("Z:/Zerg", new PrintFilePath("C:\\tempDoc\\share_data\\Zerg.txt"));

	}

	public static void start(String path, Callback callback) {
		File f = new File(path);
		if (f.isDirectory()) {
			travel(f, callback);
		}
		callback.finish();
	}

	private static void travel(File dir, Callback callback) {
		if (!dir.exists()) {
			return;
		}

		File[] files = dir.listFiles();
		for (int i=0; i<files.length; i++) {
			File file = files[i];
			if (file.isFile()) {
				callback.doSth(file);
			} else if (file.isDirectory()) {
				travel(file, callback);
			}
		}
	}

}

interface Callback {
	void doSth(File file);

	void finish();
}

class PrintFilePath implements Callback {
	List<String> lines = new ArrayList<>();
	File file;

	public PrintFilePath(String fileName) {
		file = new File(fileName);
	}
	@Override
	public void doSth(File file) {
		if (file.getName().endsWith(".ppt")
				|| file.getName().endsWith(".pptx")
				|| file.getName().endsWith(".doc")
				|| file.getName().endsWith(".docx")
				|| file.getName().endsWith(".mp4")
				|| file.getName().endsWith(".avi")
				|| file.getName().endsWith(".pdf")) {
			System.out.println(file.getAbsolutePath());
			lines.add(file.getAbsolutePath());
		}

	}

	@Override
	public void finish() {
		try {
			Base.save(lines, file, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}