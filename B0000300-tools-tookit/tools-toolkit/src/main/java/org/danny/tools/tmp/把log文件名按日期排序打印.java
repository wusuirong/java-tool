/*
 * MicroFocus.com Inc.
 * Copyright(c) 2020 All Rights Reserved.
 */
package org.danny.tools.tmp;

import java.io.*;
import java.text.ParseException;
import java.util.*;

/**
 * @author wusui
 * @version $Id: 分析日志.java, 2020-10-10 4:39 PM wusui Exp $
 */
public class 把log文件名按日期排序打印 extends Base {

    public static void main(String[] args) throws IOException, ParseException {
        String sourceDir = "F:\\cpe\\OCTIM19U860244\\1111\\Server log_10th Nov";
        File folder = new File(sourceDir);
//        String outputDir = "F:\\cpe\\OCTIM19U860244\\1111\\";

//        Calendar c = Calendar.getInstance();
//        c.set(2020, -1 + 11, 0 + 5, 0 + 22, 0 + 0, 0 + 0);

        List<String> fileNames = new ArrayList<>();
        File[] files = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return false;
                }
                if (file.getName().startsWith("WrapperProbeGw")) {
                    return true;
                } else {
                    return true;
                }
            }
        });
        Map<String, String> map = new TreeMap();
        for (File logFile : files) {
            analyzeFile(logFile, map);
        }
        printResult(map);
    }

    private static void printResult(Map<String, String> map) {
        for (Map.Entry entry : map.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }
    }

    private static void analyzeFile(File logFile, Map<String, String> timeFileNamePair) throws IOException, ParseException {
        String fileName = logFile.getName();
        BufferedReader br = new BufferedReader(new FileReader(logFile));
        String line;

        while (null != (line = br.readLine())) {
            String[] tokens = line.split(",");
            String timeStr = tokens[0];
            timeFileNamePair.put(timeStr, fileName);
            break;
        }
        br.close();
    }

}
