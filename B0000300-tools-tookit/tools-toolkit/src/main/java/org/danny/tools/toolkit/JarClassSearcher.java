package org.danny.tools.toolkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

/**
 * 功能：在指定的路径中的Jar文件中寻找自己所需要的class
 * 用法：提供本地磁盘路径和所需要的class的全称（包括package等信息）
 * 例如：Java -cp . JarClassFind Path ClassName
 * eg:  Java -cp . JarClassFind F:\JDK java.applet.Applet
 * bat command example: java -cp . org.danny.tools.toolkit.JarClassFind C:\UCMDB\UCMDBServer com.hp.ucmdb.discovery.server.service.DiscoveryResourceService
 *
 * @author suirongw
 */
public class JarClassSearcher {
    public static int count = 0;

    public JarClassSearcher() {
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
        int jarCount = 0;
        for (int i = 0; i < filelist.length; i++) {
            File temp = new File(path + filelist[i]);
            if ((temp.isDirectory() && !temp.isHidden() && temp.exists())) {
                findClassInLocalSystem(path + filelist[i], classname);
            } else {
                if (filelist[i].endsWith("jar")) {
                    jarCount++;
                    try {
                        java.util.jar.JarFile jarfile = new java.util.jar.JarFile(path + filelist[i]);
                        for (Enumeration e = jarfile.entries(); e.hasMoreElements(); ) {
                            String name = e.nextElement().toString();
                            if (name.equals(classname)) {
                                System.out.println();
                                System.out.println("-----------------");
                                System.out.println("No." + ++JarClassSearcher.count);
                                System.out.println("Jar Package:" + path + filelist[i]);
                                System.out.println(name);
                                System.out.println("-----------------");
                                System.out.println();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (jarCount > 0) {
            System.out.println("Checked " + jarCount + " jars in " + file.getAbsolutePath());
        }

    }

    static public void main(String[] args) throws IOException {
        System.out.println("I'll search for a class in a directory's jars recursively.");

//        if (args.length < 2) {
//            showHowToUsage();
//            return;
//        }
        String directoryName = getUserInput("Please input the root directory name: ");
        //args[1].replace('.', '/') + ".class";
        String canonicalClassname = getUserInput("Please input the canonical classname. E.g, org.danny.Test:");

        System.out.println();
        System.out.println("Find class [" + canonicalClassname+ "] in Path [" + directoryName+ "] Results:");

        canonicalClassname = canonicalClassname.replaceAll("\\.", "/");
        canonicalClassname = canonicalClassname.replaceAll("\\\\", "/");
        if (!canonicalClassname.endsWith(".class")) {
            canonicalClassname = canonicalClassname + ".class";
        }

        System.out.println("The classname is changed to filename format in jar: " + canonicalClassname);
        System.out.println();

        findClassInLocalSystem(directoryName, canonicalClassname);
        if (JarClassSearcher.count == 0) {
            System.out.println("Error:Can't Find Such Jar File!");
        }
        System.out.println("Find Process Ended! Total Results:" + JarClassSearcher.count);
    }

    public static void showHowToUsage() {
        System.out.println("Usage: java -cp . JarClassFind <source path> <source class name>");
        System.out.println("Usage: java -classpath . JarClassFind <source path> <source class name>");
        System.out.println("");
        System.out.println("<source path>:\t\tPath to Find eg:F:\\JDK");
        System.out.println("<source class name>:\tClass to Find eg:java.applet.Applet");
    }

    static String getUserInput(String hint) throws IOException {
        System.out.println(hint);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }
}