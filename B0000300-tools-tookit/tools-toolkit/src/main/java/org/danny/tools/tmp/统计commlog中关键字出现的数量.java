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
public class 统计commlog中关键字出现的数量 {



    public static void main(String[] args) throws IOException, ParseException {
        File folder = new File("F:\\cpe\\OCTIM19U860244_Host Resource by Shell job gets stuck in between and doesn't complete\\a20210107\\Communication Log_7th Jan");
        File[] wrapperlogs = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.startsWith("WrapperProbeGw")) {
                    return true;
                } else {
                    return true;
                }

            }
        });

        List<String> matchLines = new ArrayList();
        Set<String> fileNames = new HashSet<>();
        List<NameCount> nameCounts = new ArrayList<>();
        for (File wrapperlog : wrapperlogs) {
            analyzeFile(wrapperlog, matchLines, fileNames, nameCounts);
        }
        System.out.println("total file count: " + wrapperlogs.length);
        Collections.sort(nameCounts, new Comparator<NameCount>() {
            @Override
            public int compare(NameCount o1, NameCount o2) {
                return o1.count - o2.count;
            }
        });
        for (NameCount entry : nameCounts) {
            System.out.println(entry.name + ", " + entry.count);
        }

//        System.out.println("match file count: " + fileNames.size());
//        Collections.sort(matchLines, new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return o1.compareTo(o2);
//            }
//        });

//        StringBuilder sb = new StringBuilder();
//        for (String fileName : fileNames) {
//            sb.append(fileName).append(System.lineSeparator());
//        }
//
//        for (String line : matchLines) {
//            sb.append(line).append(System.lineSeparator());
//        }
//        File output = new File("f:\\从各种log文件里过滤出带Danny标记的log并按时间排序.txt");
//        FileUtil.save(sb.toString(), output);
//        System.out.println("再调用sort排序");
    }

    private static void analyzeFile(File wrapperlog, List<String> results, Set<String> fileNames, List<NameCount> nameCounts) throws IOException, ParseException {
//        System.out.println("Analyzing file: " + wrapperlog.getAbsolutePath());
        BufferedReader br = new BufferedReader(new FileReader(wrapperlog));
        String line;
        int count = 0;
        while (null != (line = br.readLine())) {
            if (line.contains("<object class=")) {
                count++;

            }
        }
        if (count > 0) {
            fileNames.add(wrapperlog.getName());
            NameCount nc = new NameCount();
            nc.name = wrapperlog.getName();
            nc.count = count;
            nameCounts.add(nc);
        }
    }

}

class NameCount {
    String name;
    int count;
}