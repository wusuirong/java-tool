/*
 * MicroFocus.com Inc.
 * Copyright(c) 2020 All Rights Reserved.
 */
package org.danny.tools.tmp;

import org.danny.tools.common.io.FileUtil;

import java.io.*;
import java.text.ParseException;
import java.util.*;

/**
 * @author wusui
 * @version $Id: 分析日志.java, 2020-10-10 4:39 PM wusui Exp $
 */
public class 找执行时间晚于指定时间的serverlog extends Base {

    public static void main(String[] args) throws IOException, ParseException {
        String sourceDir = "F:\\cpe\\OCTIM19U860244_Host Resource by Shell job gets stuck in between and doesn't complete\\a20210106\\Server log_6th Jan";
        String outputDir = "F:\\cpe\\OCTIM19U860244_Host Resource by Shell job gets stuck in between and doesn't complete\\a20210106\\Server log_6th Jan filterByTime";

        File srcFolder = new File(sourceDir);
        File outputFolder = new File(outputDir);
        outputFolder.mkdirs();

//        Calendar c = Calendar.getInstance();
//        c.set(2020, -1 + 11, 0 + 5, 0 + 22, 0 + 0, 0 + 0);17/12/2020 10:10 PM
        //2020-10-30 12:20:53,248
        Date earliestTime = sdfSec.parse("2021-01-05 22:25:00");

        List<String> fileNames = new ArrayList<>();
        File[] logFiles = srcFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return false;
                }
                if (file.getName().contains("wrapper.log")) {
                    return false;
                }
                if (file.length() == 0) {
                    return false;
                }
                return true;
            }
        });
        Arrays.sort(logFiles, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        for (File logFile : logFiles) {
            analyzeFile(logFile, fileNames, earliestTime, outputFolder);
        }
    }

    private static void analyzeFile(File logFile, List<String> fileNames, Date earliestTime, File outputFolder) throws IOException, ParseException {
        String fileName = logFile.getName();
        BufferedReader br = new BufferedReader(new FileReader(logFile));
        boolean match = false;
        String line;
//        System.out.println(fileName);
        while (null != (line = br.readLine())) {
            Date startTime = getTimeOfServerLog(line);
            if (startTime.after(earliestTime)) {
                System.out.println(fileName + ", " + startTime);
                match = true;
                break;
            }
        }
        br.close();
        if (match) {
            logFile.renameTo(new File(outputFolder.getAbsolutePath() + File.separator + logFile.getName()));
        }
//        System.out.println();
    }

}
