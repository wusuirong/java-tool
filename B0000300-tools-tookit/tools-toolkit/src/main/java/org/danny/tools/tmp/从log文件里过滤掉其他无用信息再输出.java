/*
 * MicroFocus.com Inc.
 * Copyright(c) 2020 All Rights Reserved.
 */
package org.danny.tools.tmp;

import org.apache.commons.lang.StringUtils;
import org.danny.tools.common.io.FileUtil;

import java.io.*;
import java.text.ParseException;
import java.util.*;

/**
 * @author wusui
 * @version $Id: 分析日志.java, 2020-10-10 4:39 PM wusui Exp $
 */
public class 从log文件里过滤掉其他无用信息再输出 extends Base {

    //JobExecuterWorker-8:IIS Applications by NTCMD_149.216.4.205
    static final String[] KEYS = new String[]{"Node links condition for node",
            "was found to have non valid node links condition",
            "Token is expired. Token should not be recreated."};//JobExecuterWorker-1:IIS Applications by NTCMD_10.208.190.13 //Danny2

    static String srcFolderName = "F:\\cpe\\OCTIM19U1146314_New server are not getting discovered by UCMDB infrastructure discovery jobs\\12.09\\server_log_Dec_03";
    static String destFolderName = srcFolderName + File.separator + "output";

    public static void main(String[] args) throws IOException, ParseException {
        File srcFolder = new File(srcFolderName);
        File destFolder = new File(destFolderName);
        destFolder.mkdir();
//        Date startTime = sdfInMillSec.parse("2020-11-26 11:50:29,000");
//        Date endTime = sdfInMillSec.parse("2020-11-26 11:50:35,000");
        String outputFileName = "f:\\error.txt";
        File[] wrapperlogs = srcFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return false;
                }
                if (file.getName().startsWith("error")) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        List<String> matchLines = new ArrayList();
        Set<String> fileNames = new HashSet<>();
        for (File wrapperlog : wrapperlogs) {
            analyzeFile(wrapperlog, matchLines, fileNames, destFolder);
        }
    }

    private static void analyzeFile(File wrapperlog, List<String> results, Set<String> fileNames, File destFolder) throws IOException, ParseException {
        System.out.println("Analyzing file: " + wrapperlog.getAbsolutePath());
        BufferedReader br = new BufferedReader(new FileReader(wrapperlog));
        String line;
        int lineCount = 0;
        StringBuilder sb = new StringBuilder();
        while (null != (line = br.readLine())) {
            lineCount++;
            if (StringUtils.isEmpty(line)) {
                continue;
            }
            boolean foundUnwantedKey = false;
            for (String key : KEYS) {
                if (line.contains(key)) {
                    foundUnwantedKey = true;
                    break;
                }
            }
            if (!foundUnwantedKey) {
                sb.append(line).append("       line: ").append(lineCount).append(System.lineSeparator());
                fileNames.add(wrapperlog.getName());
            }
        }
        File output = new File(destFolder.getAbsolutePath() + File.separator + wrapperlog.getName());
        FileUtil.save(sb.toString(), output);
    }

}
