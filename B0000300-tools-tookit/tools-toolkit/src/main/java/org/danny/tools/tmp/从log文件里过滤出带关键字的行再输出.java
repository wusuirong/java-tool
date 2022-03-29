/*
 * MicroFocus.com Inc.
 * Copyright(c) 2020 All Rights Reserved.
 */
package org.danny.tools.tmp;

import org.apache.commons.lang.StringUtils;
import org.danny.tools.common.io.FileUtil;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wusui
 * @version $Id: 分析日志.java, 2020-10-10 4:39 PM wusui Exp $
 */
public class 从log文件里过滤出带关键字的行再输出 extends Base {

    //JobExecuterWorker-8:IIS Applications by NTCMD_149.216.4.205
    static final String[] KEYS = new String[]{"4469c7e75c1375f291c5a00417f507ac"};//JobExecuterWorker-1:IIS Applications by NTCMD_10.208.190.13 //Danny2

    static String srcFolderName = "F:\\cpe\\OCTIM19U1146314_New server are not getting discovered by UCMDB infrastructure discovery jobs\\12.09\\server_log_Dec_03\\output";
    static String destFolderName = srcFolderName + File.separator + "output";

    public static void main(String[] args) throws IOException, ParseException {
        File srcFolder = new File(srcFolderName);
        File destFolder = new File(destFolderName);
        destFolder.mkdir();
//        Date startTime = sdfInMillSec.parse("2020-11-26 11:50:29,000");
//        Date endTime = sdfInMillSec.parse("2020-11-26 11:50:35,000");
        File[] wrapperlogs = srcFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return false;
                }
                if (file.getName().startsWith("error")) {
                    return true;
                } else {
                    return true;
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
            boolean foundWantedKey = false;
            for (String key : KEYS) {
                if (line.contains(key)) {
                    foundWantedKey = true;
                    break;
                }
            }
            if (foundWantedKey) {
                sb.append(line).append("       line: ").append(lineCount).append(System.lineSeparator());
                fileNames.add(wrapperlog.getName());
            }
        }
        File output = new File(destFolder.getAbsolutePath() + File.separator + wrapperlog.getName());
        FileUtil.save(sb.toString(), output);
    }

}
