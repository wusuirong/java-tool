package org.danny.tools.toolkit;

import java.io.File;
import java.util.Enumeration;

/**
 * 功能：在指定的路径中的Jar文件中寻找自己所需要的class
 * 用法：提供本地磁盘路径和所需要的class的全称（包括package等信息）
 * 例如：Java -cp . JarClassFind Path ClassName
 * eg:  Java -cp . JarClassFind F:\JDK java.applet.Applet
 * @author suirongw
 *
 */
public class 查找jar中的class {
	public static int count = 0;

	public 查找jar中的class() {
	}

	private static void findClassInLocalSystem(String path, String classname) {
		if (!path.endsWith(File.separator)) {
			path += File.separator;
		}
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("Error: Path not Existed! Please Check it out!");
			return;
		}
		String[] filelist = file.list();
		for (int i = 0; i < filelist.length; i++) {
			File temp = new File(path + filelist[i]);
			if ((temp.isDirectory() && !temp.isHidden() && temp.exists())) {
				findClassInLocalSystem(path + filelist[i], classname);
			} else {
				if (filelist[i].endsWith("jar")) {
					try {
						java.util.jar.JarFile jarfile = new java.util.jar.JarFile(path + filelist[i]);
						for (Enumeration e = jarfile.entries(); e.hasMoreElements();) {
							String name = e.nextElement().toString();
							if (name.equals(classname)) {
								System.out.println("No." + ++查找jar中的class.count);
								System.out.println("Jar Package:" + path + filelist[i]);
								System.out.println(name);
							}
						}
					} catch (Exception eee) {
					}
				}
			}
		}

	}

	static public void main(String[] args) {
		if (args.length < 2) {
			showHowToUsage();
			return;
		}
		String absoluteclassname = args[1].replace('.', '/') + ".class";

		System.out.println("Find class [" + args[1] + "] in Path [" + args[0] + "] Results:");
		findClassInLocalSystem(args[0], absoluteclassname);
		if (查找jar中的class.count == 0) {
			System.out.println("Error:Can't Find Such Jar File!");
		}
		System.out.println("Find Process Ended! Total Results:" + 查找jar中的class.count);
	}

	public static void showHowToUsage() {
		System.out.println("Usage: java -cp . JarClassFind <source path> <source class name>");
		System.out.println("Usage: java -classpath . JarClassFind <source path> <source class name>");
		System.out.println("");
		System.out.println("<source path>:\t\tPath to Find eg:F:\\JDK");
		System.out.println("<source class name>:\tClass to Find eg:java.applet.Applet");
	}
}