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
public class 从serverlog中过滤出时间晚于指定时间的log并输出到新文件 extends Base {

    public static void main(String[] args) throws IOException, ParseException {
        String sourceDir = "F:\\cpe\\OCTIM19U860244_Host Resource by Shell job gets stuck in between and doesn't complete\\12.18\\server_log_filterByTime_filterByJobName";
        String outputDir = "F:\\cpe\\OCTIM19U860244_Host Resource by Shell job gets stuck in between and doesn't complete\\12.18\\server_log_filterByTime_filterByJobName_bytime";

        File srcFolder = new File(sourceDir);
        File outputFolder = new File(outputDir);
        outputFolder.mkdirs();

//        Calendar c = Calendar.getInstance();
//        c.set(2020, -1 + 11, 0 + 5, 0 + 22, 0 + 0, 0 + 0);17/12/2020 10:10 PM
        //2020-10-30 12:20:53,248
        Date earliestTime = sdfSec.parse("2020-12-17 22:00:00");

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
        BufferedReader br = new BufferedReader(new FileReader(logFile));
        List<String> lines = new ArrayList<>();
        String line;
        boolean meetTime = false;
        while (null != (line = br.readLine())) {
            if (!meetTime) {
                Date startTime = getTimeOfServerLog(line);
                if (startTime.after(earliestTime)) {
                    meetTime = true;
                }
            }
            if (meetTime) {
                lines.add(line);
            }
        }
        br.close();
        File newFile = new File(outputFolder.getAbsolutePath() + File.separator + logFile.getName());
        BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));
        for (String str : lines) {
            bw.write(str);
            bw.newLine();
        }
        bw.close();
//        System.out.println();
    }

}
