/*
 * MicroFocus.com Inc.
 * Copyright(c) 2020 All Rights Reserved.
 */
package org.danny.tools.tmp;

import org.danny.tools.common.io.FileUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * @author wusui
 * @version $Id: 分析日志.java, 2020-10-10 4:39 PM wusui Exp $
 */
public class 根据接口调用方法和线程id提取整个执行过程的log2 extends Base {



    static class ThreadIdAndTime {
        String threadId;
        Date time;

        public ThreadIdAndTime(String threadId, Date time) {
            this.threadId = threadId;
            this.time = time;
        }
    }

    static class FileAndLineNum {
        String fileName;
        int lineNum;
        public FileAndLineNum(String fileName, int lineNum) {
            this.fileName = fileName;
            this.lineNum = lineNum;
        }
    }

    static class LineData {
        String line;
        Date time;
        Set<FileAndLineNum> fileAndLineNums = new HashSet<>();

        public LineData(String line, Date time, String fileName, int lineNum) {
            this.line = line;
            this.time = time;
            fileAndLineNums.add(new FileAndLineNum(fileName, lineNum));
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(line);
            Iterator<FileAndLineNum> it = fileAndLineNums.iterator();
            while (it.hasNext()) {
                FileAndLineNum item = it.next();
                sb.append(", file: " + item.fileName + ", line: " + item.lineNum);
            }
            return sb.toString();
        }
    }

    static class LineContainer {
        Map<String, LineData> keyAndLine = new TreeMap<>();

        public void add(LineData data) {
            if (!keyAndLine.containsKey(data.line)) {
                keyAndLine.put(data.line, data);
            } else {
                keyAndLine.get(data.line).fileAndLineNums.addAll(data.fileAndLineNums);
            }
        }

        public List<LineData> get() {
            return new ArrayList<>(keyAndLine.values());
        }
    }

    public static void main(String[] args) throws Exception {
        String logFolder = "C:\\tempDoc\\logs";
        String startTimeStr = "2020-11-18 10:50:58,000";
        String entryLogFileStr = "rest-api.log";
        String[] entryKeys = new String[]{"dataflowmanagement/probes/DataFlowProbe/ranges", "POST"};
        String[] fileBlackList = new String[]{"cmdb.dal", "cmdb.tql.calculation.audit", "urm_audit"};
        String resultFileStr = "C:\\tempDoc\\1.txt";

        Date startTime = sdfInMillSec.parse(startTimeStr);

        File entryLog = new File(logFolder + File.separator + entryLogFileStr);

        ThreadIdAndTime tidAndTime = getEntryThreadIdAndTime(entryLog, startTime, entryKeys);

        File folder = new File(logFolder);

        File[] logFiles = getLogFiles(folder, fileBlackList);

        LineContainer lineContainer = new LineContainer();

        getAllLogsOfThreadId(tidAndTime, logFiles, lineContainer);

        sort(lineContainer);

        saveToFile(lineContainer, new File(resultFileStr));

//        List<String> matchLines = new ArrayList();
//        Set<String> fileNames = new HashSet<>();
//        for (File wrapperlog : wrapperlogs) {
//            analyzeFile(wrapperlog, matchLines, fileNames);
//        }
//        System.out.println("lines: " + matchLines.size());
//        Collections.sort(matchLines, new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return o1.compareTo(o2);
//            }
//        });
//
//        StringBuilder sb = new StringBuilder();
//        for (String fileName : fileNames) {
//            sb.append(fileName).append(System.lineSeparator());
//        }
//
//        for (String line : matchLines) {
//            line = line.replace("(?:?) ", "");
//            sb.append(line).append(System.lineSeparator());
//        }
//        File output = new File("f:\\从各种log文件里过滤出带Danny标记的log并按时间排序.txt");
//        FileUtil.save(sb.toString(), output);
//        System.out.println("再调用sort排序");
    }

    private static void saveToFile(LineContainer lineContainer, File file) throws IOException {
        List<LineData> datas = lineContainer.get();
        List<String> lines = new ArrayList<>();
        for (LineData data : datas) {
            lines.add(data.toString());
        }
        save(lines, file, false);
    }

    private static void sort(LineContainer lineContainer) {
        List<LineData> matchLines = lineContainer.get();
        matchLines.sort(new Comparator<LineData>() {
            @Override
            public int compare(LineData o1, LineData o2) {
                int c = o1.time.compareTo(o2.time);
                if (o1.line.contains("currentStatus is CONNECTED") || o2.line.contains("currentStatus is CONNECTED")) {
                    System.out.println("hit");
                }
                if (0 == c) {
                    for (FileAndLineNum fl1 : o1.fileAndLineNums) {
                        for (FileAndLineNum fl2 : o2.fileAndLineNums) {
                            if (fl1.fileName.equals(fl2.fileName)) {
                                System.out.println("c==0 fl1.lineNum: " + fl1.lineNum + ", fl2.lineNum: " + fl2.lineNum + ",        " + o1.line + " ----- " + o2.line);
                                return fl2.lineNum - fl1.lineNum;
                            }
                        }
                    }
                    System.out.println("c==0, not same file " + o1.line + " ----- " + o2.line);
                    return 0;
                } else {
                    // 时间不相等，按时间排序
                    System.out.println(c + " " + o1.line + " ----- " + o2.line);
                    return c;
                }
            }
        });
    }

    private static void getAllLogsOfThreadId(ThreadIdAndTime tidAndTime, File[] logFiles, LineContainer lineContainer) throws ParseException {

        for (File logFile : logFiles) {
            List<String> lines = FileUtil.readTxtFileAsStrings(logFile);
            for (int i=0; i<lines.size(); i++) {
                String line = lines.get(i);
                Date time = null;
                try {
                    time = getTimeOfLog(line);
                } catch (Exception e) {
                    System.out.println("Parse log time fail on: " + line + ", file: " + logFile.getName() + ", line: " + i);
                    continue;
                }
                if (time.before(tidAndTime.time)) {
                    continue;
                }
                if (lineContainsAllKeys(line, new String[]{tidAndTime.threadId})) {
                    LineData data = new LineData(line, time, logFile.getName(), i);
                    lineContainer.add(data);
                }
            }
        }
    }

    private static File[] getLogFiles(File folder, String[] fileNameBlacklist) {
        return folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (null == fileNameBlacklist || 0 == fileNameBlacklist.length) {
                    return true;
                }
                for (int i=0; i < fileNameBlacklist.length; i++) {
                    if (name.startsWith(fileNameBlacklist[i])) {
                        return false;
                    }
                }
                return true;
            }
        });
    }

    private static ThreadIdAndTime getEntryThreadIdAndTime(File entryLog, Date startTime, String[] entryKeys) throws Exception {

        List<String> lines = FileUtil.readTxtFileAsStrings(entryLog);
        for (String line : lines) {
            Date time = getTimeOfLog(line);
            if (time.before(startTime)) {
                continue;
            }
            if (lineContainsAllKeys(line, entryKeys)) {
                return new ThreadIdAndTime(getThreadId(line), time);
            }
        }
        throw new Exception("Thread id not found.");
    }

    private static String getThreadId(String line) {
        int start = line.indexOf('[');
        int end = line.indexOf(']');
        return line.substring(start+1, end);
    }

    private static boolean lineContainsAllKeys(String line, String[] entryKeys) {
        for (String key : entryKeys) {
            if (!line.contains(key)) {
                return false;
            }
        }
        return true;
    }



//    private static void analyzeFile(File wrapperlog, List<String> results, Set<String> fileNames) throws IOException, ParseException {
//        System.out.println("Analyzing file: " + wrapperlog.getAbsolutePath());
//        BufferedReader br = new BufferedReader(new FileReader(wrapperlog));
//        String line;
//        boolean meetTag = false;
//        StringBuilder sb = new StringBuilder();
//        int lineCount = 0;
//        while (null != (line = br.readLine())) {
//            if (meetTag == false && line.contains("Danny2")) {
//                sb.append(line + "   fileName: " + wrapperlog.getName());
////                results.add(line + "   fileName: " + wrapperlog.getName());
//                fileNames.add(wrapperlog.getName());
//                meetTag = true;
//            } else if (meetTag == true && line.contains("Danny2")) {
//                results.add(sb.toString() + "   remain lines: " + lineCount);
//                lineCount = 0;
//                sb = new StringBuilder();
//                sb.append(line + "   fileName: " + wrapperlog.getName());
////                results.add(line + "   fileName: " + wrapperlog.getName());
//                fileNames.add(wrapperlog.getName());
//                meetTag = true;
//            } else if (meetTag == true && !line.contains("Danny2") && !line.startsWith("<2020")) {
//                lineCount++;
////                if (line.length() > 20) {
////                    sb.append(" -lb- ").append(line.substring(0, 20)).append("...");
////                } else {
////                    sb.append(" -lb- ").append(line);
////                }
//            } else if (meetTag == true && line.contains("Danny2") && line.startsWith("<2020")) {
//                results.add(sb.toString() + "   remain lines: " + lineCount);
//                lineCount = 0;
//                sb = new StringBuilder();
//                meetTag = false;
//            }
//        }
//        if (sb.length() > 0) {
//            results.add(sb.toString() + "   remain lines: " + lineCount);
//        }
//    }

}
