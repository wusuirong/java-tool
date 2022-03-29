/*
 * MicroFocus.com Inc.
 * Copyright(c) 2020 All Rights Reserved.
 */
package org.danny.tools.tmp;

import org.danny.tools.common.io.FileUtil;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author wusui
 * @version $Id: 分析日志.java, 2020-10-10 4:39 PM wusui Exp $
 */
public class 从stuck信息过滤commlog到指定目录 extends Base {

    public static void main(String[] args) throws IOException, ParseException {
        String stuckInfoFileName = "f:\\probegwlog找stuck信息.txt";
        String commlogDir = "F:\\cpe\\OCTIM19U860244\\10.30\\Communication Log_30th Oct";
        String outputDir = "F:\\cpe\\OCTIM19U860244\\10.30\\";
        File logfile = new File(stuckInfoFileName);

        List<String> ids = new ArrayList();
        analyzeFile(logfile, ids);

        copyFiles(commlogDir, outputDir, ids);

    }

    private static void analyzeFile(File logfile, List<String> results) throws IOException, ParseException {
        System.out.println("Analyzing file: " + logfile.getAbsolutePath());
        BufferedReader br = new BufferedReader(new FileReader(logfile));
        String line;
        while (null != (line = br.readLine())) {
            if (line.contains("Detected stuck Worker")) {
                String id = line.substring(line.indexOf("Id:") + 3, line.indexOf("which has been running for") - 1);
                results.add(id);
            }
        }
    }

}
