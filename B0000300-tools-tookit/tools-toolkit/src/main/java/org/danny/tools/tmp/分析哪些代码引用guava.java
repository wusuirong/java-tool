/*
 * MicroFocus.com Inc.
 * Copyright(c) 2020 All Rights Reserved.
 */
package org.danny.tools.tmp;

import org.danny.tools.common.io.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wusui
 * @version $Id: 分析日志.java, 2020-10-10 4:39 PM wusui Exp $
 */
public class 分析哪些代码引用guava {

    public static void main(String[] args) throws IOException, ParseException {
        searchFiles("O:\\mf\\0012-cms-ucmdb-latest\\dc");
    }

    public static void searchFiles(String path) throws IOException, ParseException {
        File f = new File(path);
        Set<String> fileNames = new HashSet<>();
        Set<String> importClasses = new HashSet<>();
        Set<String> compileErrorClassFiles = new HashSet<>();
        if (f.isDirectory()) {
            searchFilesInner(f, fileNames, importClasses, compileErrorClassFiles);
        }

        {
            StringBuilder sb = new StringBuilder();
            for (String name : fileNames) {
                sb.append(name).append(System.lineSeparator());
            }
            File output = new File("O:\\filesContainGuava.txt");
            FileUtil.save(sb.toString(), output);

            sb = new StringBuilder();
            for (String name : importClasses) {
                sb.append(name).append(System.lineSeparator());
            }
            File output2 = new File("O:\\GuavaClass.txt");
            FileUtil.save(sb.toString(), output2);

            sb = new StringBuilder();
            for (String name : compileErrorClassFiles) {
                sb.append(name).append(System.lineSeparator());
            }
            File output3 = new File("O:\\compileErrorClassFiles.txt");
            FileUtil.save(sb.toString(), output3);
        }

    }

    private static void searchFilesInner(File dir, Set<String> fileNames, Set<String> importClasses, Set<String> compileErrorClassFiles) throws IOException, ParseException {
        if (!dir.exists()) {
            return;
        }

        File[] files = dir.listFiles();
        for (int i=0; i<files.length; i++) {
            File file = files[i];
            if (file.isFile()) {
                if (file.getName().endsWith(".java")) {
                    analyzeFile(file, fileNames, importClasses, compileErrorClassFiles);
                }
            } else if (file.isDirectory()) {
                searchFilesInner(file, fileNames, importClasses, compileErrorClassFiles);
            }
        }
    }

    private static void analyzeFile(File file, Set<String> fileNames, Set<String> importClasses, Set<String> compileErrorClassFiles) throws IOException, ParseException {
        String fileName = file.getAbsolutePath();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while (null != (line = br.readLine())) {
            if (line.contains("import") && line.contains("com.google.common")) {
                fileNames.add(fileName);
                importClasses.add(line);
            }
            if (line.contains("new Predicate<")) {
                compileErrorClassFiles.add(fileName);
            }
        }
    }

}
