/*
 * MicroFocus.com Inc.
 * Copyright(c) 2020 All Rights Reserved.
 */
package org.danny.tools.tmp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author wusui
 * @version $Id: 分析日志.java, 2020-10-10 4:39 PM wusui Exp $
 */
public class 寻找执行时间长的commlog extends Base {

    public static void main(String[] args) throws IOException, ParseException {
        String sourceDir = "F:\\cpe\\OCTIM19U860244\\1111\\Communication Log_10th Nov";
        File folder = new File(sourceDir);
        String outputDir = "F:\\cpe\\OCTIM19U860244\\1111\\";

        Calendar c = Calendar.getInstance();
        c.set(2020, -1 + 11, 0 + 5, 0 + 22, 0 + 0, 0 + 0);

        List<String> fileNames = new ArrayList<>();
        File[] commlogs = folder.listFiles();
        for (File commlog : commlogs) {
            analyzeFileProcessTime(commlog, fileNames, c.getTime());
        }
        copyFiles(sourceDir, outputDir, fileNames);
    }

    private static void analyzeFileProcessTime(File commlog, List<String> fileNames, Date customerBeginTime) throws IOException, ParseException {
        String fileName = commlog.getName();
        BufferedReader br = new BufferedReader(new FileReader(commlog));
        String line;
        Date startTime = null, endTime = null;
        boolean meet1stExecutionCurrentTime = false, meetConnect = false, meetDisconnect = false, meet2ndExecutionCurrentTime = false;
        List<String> matchLines = new ArrayList<>();

        while (null != (line = br.readLine())) {
            if (line.contains("<CONNECT start=")) {
                meetConnect = true;
            } else if (line.contains("<DISCONNECT start=")) {
                meetDisconnect = true;
            } else if (line.contains("Execution current time")) {
                if (!meet1stExecutionCurrentTime) {
                    meet1stExecutionCurrentTime = true;
                    String time = line.substring(line.indexOf("Execution current time:")+23, line.indexOf("</log>"));
                    startTime = sdfSec.parse(time);
                } else {
                    meet2ndExecutionCurrentTime = true;
                    String time = line.substring(line.indexOf("Execution current time:")+23, line.indexOf("</log>"));
                    endTime = sdfSec.parse(time);
                    break;
                }
            } else if (line.contains("Batch commit") || line.contains("PluginEngine found")) {
                matchLines.add(line);
            }
        }

        boolean inTimeWindow = true;
        if (startTime.before(customerBeginTime)) {
            inTimeWindow = false;
        }

        long gap = (endTime.getTime() - startTime.getTime())/1000;
        if (gap > 900) {
            fileNames.add(fileName);
            if (inTimeWindow) {
                System.out.println(fileName + ", timeout is " + gap);
            } else {
                System.out.println(fileName + ", timeout is " + gap + " but start time not in time window: " + startTime);
            }
            for (String match : matchLines) {
                System.out.println(match);
            }
        }
    }

}
