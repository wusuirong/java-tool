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
public class 找有问题的commlog extends Base {

    public static void main(String[] args) throws IOException, ParseException {
        String sourceDir = "F:\\cpe\\OCTIM19U786269 IIS job recon error - wrong content_location for configuration_codument CI\\12.01\\IIS Applications by NTCMD";
        File folder = new File(sourceDir);

        Calendar c = Calendar.getInstance();
        c.set(2020, -1 + 11, 0 + 5, 0 + 22, 0 + 0, 0 + 0);

        List<String> fileNames = new ArrayList<>();
        File[] commlogs = folder.listFiles();
        for (File commlog : commlogs) {
            analyzeFile(commlog, fileNames, c.getTime());
        }
    }

    private static void analyzeFile(File commlog, List<String> fileNames, Date customerBeginTime) throws IOException, ParseException {
        String fileName = commlog.getName();
        BufferedReader br = new BufferedReader(new FileReader(commlog));
        String line;
        boolean meetExecEnd = false;
        int count = 0;
        while (null != (line = br.readLine())) {
            if (line.contains("</EXEC>")) {
                meetExecEnd = true;
            } else if (line.contains("<EXEC") && meetExecEnd) {
                System.out.println(fileName + ", " + count);
                meetExecEnd = false;
            } else {
                meetExecEnd = false;
            }
            count++;
        }

    }

}
