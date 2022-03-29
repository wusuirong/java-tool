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
public class 统计commlog中导致recon超时的重名result数量 {



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

    }

    private static void analyzeFile(File wrapperlog, List<String> results, Set<String> fileNames, List<NameCount> nameCounts) throws IOException, ParseException {
//        System.out.println("Analyzing file: " + wrapperlog.getAbsolutePath());
        BufferedReader br = new BufferedReader(new FileReader(wrapperlog));
        String line;
        Map<String, Integer> sameProcessStat = new HashMap<>();
        String ip = "null";
        while (null != (line = br.readLine())) {
            if (line.contains("<destinationData name=\"ip_address\">")) {
                 ip = line.substring(line.indexOf("ip_address")+12, line.indexOf("</destinationData>"));
            } else if (line.contains("<attribute name=\"process_cmdline\"")) {
                if (sameProcessStat.containsKey(line)) {
                    sameProcessStat.put(line, sameProcessStat.get(line) + 1);
                } else {
                    sameProcessStat.put(line, 1);
                }
            }
        }
        for (Map.Entry<String, Integer> entry : sameProcessStat.entrySet()) {
            if (entry.getValue() > 100) {
                NameCount nc = new NameCount();
                nc.name = ip + " " + wrapperlog.getName() + " " + entry.getKey();
                nc.count = entry.getValue();
                nameCounts.add(nc);
            }
        }
    }

}