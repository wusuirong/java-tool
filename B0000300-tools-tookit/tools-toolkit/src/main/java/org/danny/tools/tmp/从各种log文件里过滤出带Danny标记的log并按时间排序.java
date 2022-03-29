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
public class 从各种log文件里过滤出带Danny标记的log并按时间排序 extends Base {

    //JobExecuterWorker-8:IIS Applications by NTCMD_149.216.4.205
    static final String KEY = "[Process Results Thread2-MZ_Schedule Job Discovery_Host Resources_Host Resources and Applications by Shell]";//JobExecuterWorker-1:IIS Applications by NTCMD_10.208.190.13 //Danny2

    public static void main(String[] args) throws IOException, ParseException {
        File folder = new File("F:\\cpe\\OCTIM19U860244_Host Resource by Shell job gets stuck in between and doesn't complete\\a20210107\\Server log_7th Jan");
        Date startTime = sdfInMillSec.parse("2020-11-26 11:50:29,000");
        Date endTime = sdfInMillSec.parse("2021-01-06 18:25:55,623");
        String outputFileName = "F:\\cpe\\OCTIM19U860244_Host Resource by Shell job gets stuck in between and doesn't complete\\a20210107\\Server log_7th Jan-errorsample.txt";
        File[] wrapperlogs = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return false;
                }
                if (file.getName().startsWith("probeMgr-adaptersDebug") || file.getName().startsWith("probeMgr-services")) {
                    return true;
                } else {
                    return true;
                }
            }
        });

        List<String> matchLines = new ArrayList();
        Set<String> fileNames = new HashSet<>();
        for (File wrapperlog : wrapperlogs) {
            analyzeFile(wrapperlog, matchLines, fileNames, startTime, endTime);
        }
        System.out.println("lines: " + matchLines.size());
        Collections.sort(matchLines, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        StringBuilder sb = new StringBuilder();
        for (String fileName : fileNames) {
            sb.append(fileName).append(System.lineSeparator());
        }

        for (String line : matchLines) {
            line = line.replace("(?:?) ", "");
            sb.append(line).append(System.lineSeparator());
        }
        File output = new File(outputFileName);
        FileUtil.save(sb.toString(), output);
//        System.out.println("再调用sort排序");
    }

    private static void analyzeFile(File wrapperlog, List<String> results, Set<String> fileNames, Date startTime, Date endTime) throws IOException, ParseException {
        System.out.println("Analyzing file: " + wrapperlog.getAbsolutePath());
        BufferedReader br = new BufferedReader(new FileReader(wrapperlog));
        String line;
        boolean meetTag = false;
        StringBuilder sb = new StringBuilder();
        int lineCount = 0;
        while (null != (line = br.readLine())) {

            if (meetTag == false && line.contains(KEY)) {
//                Date time = getTimeOfLog(line);
//                if (time.before(startTime) || time.after(endTime)) {
//                    continue;
//                }
                sb.append(line + "   fileName: " + wrapperlog.getName());
//                results.add(line + "   fileName: " + wrapperlog.getName());
                fileNames.add(wrapperlog.getName());
                meetTag = true;
            } else if (meetTag == true && line.contains(KEY)) {

                results.add(sb.toString() + "   remain lines: " + lineCount);
                lineCount = 0;
                sb = new StringBuilder();
                sb.append(line + "   fileName: " + wrapperlog.getName());
//                results.add(line + "   fileName: " + wrapperlog.getName());
                fileNames.add(wrapperlog.getName());
                meetTag = true;
            } else if (meetTag == true && !line.contains(KEY) && !line.startsWith("<2020")) {
                lineCount++;
//                if (line.length() > 20) {
//                    sb.append(" -lb- ").append(line.substring(0, 20)).append("...");
//                } else {
//                    sb.append(" -lb- ").append(line);
//                }
            } else if (meetTag == true && line.contains(KEY) && line.startsWith("<2020")) {
                results.add(sb.toString() + "   remain lines: " + lineCount);
                lineCount = 0;
                sb = new StringBuilder();
                meetTag = false;
            }
        }
        if (sb.length() > 0) {
            results.add(sb.toString() + "   remain lines: " + lineCount);
        }
    }

}
