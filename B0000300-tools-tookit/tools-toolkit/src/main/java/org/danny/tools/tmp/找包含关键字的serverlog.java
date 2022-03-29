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
public class 找包含关键字的serverlog extends Base {

    static String keyword = "The triggers are";

    public static void main(String[] args) throws IOException, ParseException {
        String sourceDir = "F:\\cpe\\OCTIM19U1148064 Inventory Discovery by Scan Job progress goes inexplicably to 90% less than 50%\\a20210113\\log";
        String outputDir = "F:\\cpe\\OCTIM19U1148064 Inventory Discovery by Scan Job progress goes inexplicably to 90% less than 50%\\a20210113\\log_filterByKeyword";

        File srcFolder = new File(sourceDir);
        File outputFolder = new File(outputDir);
        outputFolder.mkdirs();

//        Calendar c = Calendar.getInstance();
//        c.set(2020, -1 + 11, 0 + 5, 0 + 22, 0 + 0, 0 + 0);17/12/2020 10:10 PM
        //2020-10-30 12:20:53,248
//        Date earliestTime = sdfSec.parse("2020-12-17 22:00:00");

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
            analyzeFile(logFile, fileNames, outputFolder);
        }
    }

    private static void analyzeFile(File logFile, List<String> fileNames, File outputFolder) throws IOException, ParseException {
        String fileName = logFile.getName();
        BufferedReader br = new BufferedReader(new FileReader(logFile));
        boolean match = false;
        String line;
//        System.out.println(fileName);
        while (null != (line = br.readLine())) {
            if (line.contains(keyword)) {
                System.out.println(fileName);
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
