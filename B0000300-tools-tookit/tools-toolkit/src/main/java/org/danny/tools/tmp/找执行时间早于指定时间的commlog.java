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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wusui
 * @version $Id: 分析日志.java, 2020-10-10 4:39 PM wusui Exp $
 */
public class 找执行时间早于指定时间的commlog extends Base {

    public static void main(String[] args) throws IOException, ParseException {
        String sourceDir = "F:\\cpe\\OCTIM19U860244_Host Resource by Shell job gets stuck in between and doesn't complete\\12.18\\Server log_18th Dec";
        File folder = new File(sourceDir);

//        Calendar c = Calendar.getInstance();
//        c.set(2020, -1 + 11, 0 + 5, 0 + 22, 0 + 0, 0 + 0);17/12/2020 10:10 PM
        Date earliestTime = sdfSec.parse("2020-12-17 22:00:00");

        List<String> fileNames = new ArrayList<>();
        File[] commlogs = folder.listFiles();
        for (File commlog : commlogs) {
            analyzeFile(commlog, fileNames, earliestTime);
        }
    }

    private static void analyzeFile(File commlog, List<String> fileNames, Date earliestTime) throws IOException, ParseException {
        String fileName = commlog.getName();
        BufferedReader br = new BufferedReader(new FileReader(commlog));
        String line;
        boolean meetDisconn = false;
        boolean done = false;
        int count = 0;
        while (null != (line = br.readLine())) {
            if (line.contains("<DISCONNECT start")) {
                meetDisconn = true;
            } else if (line.contains("Execution current time") && meetDisconn) {
                String time = line.substring(line.indexOf("Execution current time:")+23, line.indexOf("</log>"));
                Date startTime = sdfInCommlog.parse(time);
                if (startTime.before(earliestTime)) {
                    System.out.println(fileName + ", " + startTime);
                }
                done = true;
            }
            count++;
        }
        if (!done) {
//            System.out.println("Not finish file: " + fileName);
        }
    }

}
