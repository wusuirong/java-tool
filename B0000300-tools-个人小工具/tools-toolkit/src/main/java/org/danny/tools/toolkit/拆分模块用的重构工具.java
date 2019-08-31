package org.danny.tools.toolkit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.StringUtils;
import org.danny.tools.common.cli.CommandLineUtil;
import org.danny.tools.common.io.FileUtil;
import org.danny.tools.common.lang.Detect;
/**
 * 重构工具
 * 拆分模块用的
 * 要对大模块a进行拆分，分为2个小模块lowerModule和upperModule时，且lowerModule为底层模块，upperModule为高层模块
 * 用户按自己需要先把lowerModule的核心代码先分离出来，此时lowerModule中的代码可能会有对upperModule模块的引用，即底层对高层的反向引用
 * 则用户就需要把upperModule层中被引用的代码移到lowerModule中，这个工具就是做这件事情的
 * 它主要通过对lowerModule模块的java代码的import语句进行分析，把引用到upperModule的代码移到lowerModule中
 * @author Administrator
 *
 */
public class 拆分模块用的重构工具 {
	
	private String upperModuleName;
	
	private String lowerModuleName;
	
	int count = 0;
	
	public static void main(String[] args) throws Exception {
		拆分模块用的重构工具 拆分模块用的重构工具 = new 拆分模块用的重构工具();
		拆分模块用的重构工具.welcome();
		
		拆分模块用的重构工具.run(true);
		System.out.println("执行完毕, 共移动" + 拆分模块用的重构工具.count + "个文件");
	}
	
	public void welcome() throws IOException {
		System.out.println("************************************");
		System.out.println("重构工具");
		System.out.println("拆分模块用的");
		System.out.println("要对大模块a进行拆分，分为2个小模块lowerModule和upperModule时，且lowerModule为底层模块，upperModule为高层模块");
		System.out.println("用户按自己需要先把lowerModule的核心代码先分离出来，此时lowerModule中的代码可能会有对upperModule模块的引用，即底层对高层的反向引用");
		System.out.println("则用户就需要把upperModule层中被引用的代码移到lowerModule中，这个工具就是做这件事情的");
		System.out.println("它主要通过对lowerModule模块的java代码的import语句进行分析，把引用到upperModule的代码移到lowerModule中");
		System.out.println("************************************");
		
		System.out.println("输入高层模块文件夹");
		upperModuleName = CommandLineUtil.readLine();
		System.out.println("输入底层模块文件夹");
		lowerModuleName = CommandLineUtil.readLine();
	}
	
	/**
	 * 对lowerModule模块的java代码的import语句进行分析，把引用到upperModule的代码移到lowerModule中
	 * @param isCut 是否通过剪切方式把代码移到lowerModule中，true是剪切，false是copy
	 * @throws Exception 
	 */
	public void run(boolean isCut) throws Exception {
		File lowerModule = new File(lowerModuleName);
		File upperModule = new File(upperModuleName);
		
		while(true) {
			Set<String> missingReferenceJavaClasses = scanMissingReferenceJavaFiles(lowerModule);
			Set<String> existsJavaClasses = findAllJavaClasses(lowerModule);
			missingReferenceJavaClasses.removeAll(existsJavaClasses);
			if (Detect.notEmpty(missingReferenceJavaClasses)) {
				List<File> javaFiles = findMatchJavaFiles(upperModule, missingReferenceJavaClasses);
				if (Detect.notEmpty(javaFiles)) {
					for (File f : javaFiles) {
						if (isCut) {
							moveFileFromDirToDir(f, upperModule, lowerModule, true);
						} else {
							moveFileFromDirToDir(f, upperModule, lowerModule, false);
						}
					}					
				} else {//没有可复制的文件则退出
					break;
				}
			}
		}
		
	}
	
	/**
	 * 扫描module里的java文件，对import进行分析，返回不存在module中的import类
	 * 比如import com.sun.AClass; 返回 com.sun.AClass
	 * 这个扫描只是比较粗略的扫描，因为有些import类是引用jdk中或第三方jar的，这些也会返回
	 * 但返回的结果的用途是为了从upperModule中找class，所以没关系
	 * @param module
	 * @return
	 */
	Set<String> scanMissingReferenceJavaFiles(File module) {
		Collection<File> javaFiles = FileUtils.listFiles(module, new SuffixFileFilter(".java"), TrueFileFilter.INSTANCE);
		
		Set<String> javaClassSet = new HashSet<String>();
		Set<String> importClassSet = new HashSet<String>();
		for (File f : javaFiles) {
			String subPath = FileUtil.subPath(module, f);
			subPath = StringUtils.removeEnd(subPath, ".java");
			subPath = StringUtils.replace(subPath, File.separator, ".");
			javaClassSet.add(subPath);
			
			List<String> importClasses = analyseJavaFileDependency(f);
			importClassSet.addAll(importClasses);
		}
		
		return importClassSet;
	}
	
	/**
	 * 在指定目录里查找符合javaClassName的java类文件
	 * 比如com.sun.AClass，则在目录下查找com/sun/AClass.java
	 * @param dir
	 * @param javaClassNames
	 * @return
	 */
	List<File> findMatchJavaFiles(File dir, Set<String> javaClassNames) {
		Set<String> set = new HashSet<String>();
		for (String javaClassName : javaClassNames) {
			String key = StringUtils.replace(javaClassName, ".", File.separator) + ".java";
			set.add(key);
		}
		Collection<File> javaFiles = FileUtils.listFiles(dir, new SuffixFileFilter(".java"), TrueFileFilter.INSTANCE);
		
		List<File> matchFiles = new ArrayList<File>();
		for (File f : javaFiles) {
			String subPath = FileUtil.subPath(dir, f);
			if (set.contains(subPath)) {
				matchFiles.add(f);
			}
		}
		return matchFiles;
	}
	
	/**
	 * 查找目录下的java类，并把类名com.sun.AClass这样的字符串放入结果集
	 * @param dir
	 * @return
	 */
	Set<String> findAllJavaClasses(File dir) {
		Set<String> set = new HashSet<String>();
		
		Collection<File> javaFiles = FileUtils.listFiles(dir, new SuffixFileFilter(".java"), TrueFileFilter.INSTANCE);
		for (File f : javaFiles) {
			String subPath = FileUtil.subPath(dir, f);
			subPath = StringUtils.removeEnd(subPath, ".java");
			subPath = StringUtils.replace(subPath, File.separator, ".");
			set.add(subPath);
		}
		return set;
	}
	
	boolean moveFileFromDirToDir(File file, File fromDir, File toDir, boolean cut) throws Exception {
		String subPath = FileUtil.subPath(fromDir, file);
		
		String fullPath = toDir.getAbsolutePath() + File.separator + subPath;
		File destDir = new File(StringUtils.substringBeforeLast(fullPath, File.separator));
		destDir.mkdirs();
		File destFile = new File(fullPath);
		if (cut) {
			System.out.println("移动文件\r\n" + "src =" + file.getAbsolutePath() + "\r\ndest=" + destFile.getAbsolutePath());
			if (!file.renameTo(destFile)) {
				throw new Exception("移动文件失败\r\n" + "src =" + file.getAbsolutePath() + "\r\ndest=" + destFile.getAbsolutePath());
			}			
		} else {
			System.out.println("复制文件\r\n" + "src =" + file.getAbsolutePath() + "\r\ndest=" + destFile.getAbsolutePath());
			FileUtils.copyFile(file, destFile);
		}
		count++;
		return true;
	}
	
	List<String> analyseJavaFileDependency(File javaFile) {
		List<String> dependencies = new ArrayList<String>();
		List<String> lines = FileUtil.readTxtFileAsStrings(javaFile);
		for (String line : lines) {
			line = line.trim();
			if (line.startsWith("import ")) {
				line = StringUtils.remove(line, "import");
				line = StringUtils.remove(line, ";");
				line = line.trim();
				dependencies.add(line);
			}
		}
		return dependencies;
	}
}
