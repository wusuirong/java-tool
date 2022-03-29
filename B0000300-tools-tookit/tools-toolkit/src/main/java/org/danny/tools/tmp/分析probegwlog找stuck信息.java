/*
 * MicroFocus.com Inc.
 * Copyright(c) 2020 All Rights Reserved.
 */
package org.danny.tools.tmp;

import org.danny.tools.common.io.FileUtil;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wusui
 * @version $Id: 分析日志.java, 2020-10-10 4:39 PM wusui Exp $
 */
public class 分析probegwlog找stuck信息 {

    public static void main(String[] args) throws IOException, ParseException {
        File folder = new File("F:\\cpe\\OCTIM19U860244\\1111\\Probe log_10th Nov");
        File[] wrapperlogs = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return false;
                }
                if (file.getName().startsWith("WrapperProbeGw")) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        Arrays.sort(wrapperlogs, 0, wrapperlogs.length, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                long m1 = o1.lastModified();
                long m2 = o2.lastModified();
                return (m1-m2)>0 ? 1 : -1;
            }
        });
        List<String> matchLines = new ArrayList();
        for (File wrapperlog : wrapperlogs) {
            analyzeFile(wrapperlog, matchLines);
        }

        StringBuilder sb = new StringBuilder();
        for (String line : matchLines) {
            sb.append(line).append(System.lineSeparator());
        }
        File output = new File("f:\\cpe\\probegwlog找stuck信息.txt");
        FileUtil.save(sb.toString(), output);
    }

    private static void analyzeFile(File wrapperlog, List<String> results) throws IOException, ParseException {
        System.out.println("Analyzing file: " + wrapperlog.getAbsolutePath());
        BufferedReader br = new BufferedReader(new FileReader(wrapperlog));
        String line;
        while (null != (line = br.readLine())) {
            if (line.contains("Detected stuck Worker")) {
                results.add(line);
            } else if (line.contains(" Current detected total")) {
                results.add(line);
            } else if (line.contains("Probe is being Restarted")) {
                results.add(line);
            }
        }
    }

}
