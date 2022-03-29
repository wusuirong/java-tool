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
public class 根据接口调用方法和线程id提取整个执行过程的log extends Base {

    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss,SSS";

    static class ThreadIdAndTime {
        String threadId;
        Date time;

        public ThreadIdAndTime(String threadId, Date time) {
            this.threadId = threadId;
            this.time = time;
        }
    }

//    static class FileAndLineNum {
//        String fileName;
//        int lineNum;
//        public FileAndLineNum(String fileName, int lineNum) {
//            this.fileName = fileName;
//            this.lineNum = lineNum;
//        }
//    }

    static class LineData {
        String line;
        Date time;
        String fileName;
        int lineNum;
//        Set<FileAndLineNum> fileAndLineNums = new HashSet<>();

        public LineData(String line, Date time, String fileName, int lineNum) {
            this.line = line;
            this.time = time;
            this.fileName = fileName;
            this.lineNum = lineNum;
//            fileAndLineNums.add(new FileAndLineNum(fileName, lineNum));
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(line);
//            Iterator<FileAndLineNum> it = fileAndLineNums.iterator();
//            while (it.hasNext()) {
//                FileAndLineNum item = it.next();
//                sb.append(", file: " + item.fileName + ", line: " + item.lineNum);
//            }
            sb.append(", file: " + fileName + ", line: " + lineNum);
            return sb.toString();
        }
    }

    static class LineContainer {
        //        Map<String, LineData> keyAndLine = new TreeMap<>();
        List<LineData> lines = new ArrayList<>();
        public void add(LineData data) {
//            if (!keyAndLine.containsKey(data.line)) {
//                keyAndLine.put(data.line, data);
//            } else {
//                keyAndLine.get(data.line).fileAndLineNums.addAll(data.fileAndLineNums);
//            }
            lines.add(data);
        }

        public List<LineData> get() {
//            return new ArrayList<>(keyAndLine.values());
            return lines;
        }
    }

    static class ThreadLogContext {
        Date startTime;
        Date endTime;
        String[] entryKeys;

        ThreadLogContext(Date startTime, Date endTime, String[] entryKeys) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.entryKeys = entryKeys;
        }
    }

    public static void main(String[] args) throws Exception {
        String logFolder = "C:\\tempDoc\\logs";
        String[] fileBlackList = new String[]{"cmdb.dal", "cmdb.tql.calculation.audit", "urm_audit",
                "cmdb.ha.detailed", "mam.collectors.dal",
                "discovery-server"};
        //sql should not print in discovery-server.log

        String entryLogFileStr = "rest-api.log";
        String resultFileStr = "C:\\tempDoc\\1.txt";
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);

//        String startTimeStr = "2020-11-19 09:20:52,000";
//        String endTimeStr = "2020-11-19 09:20:55,000";
//        String[] entryKeys = new String[]{"dataflowmanagement/ranges", "DELETE"};

//        Date startTime = sdfInMillSec.parse(startTimeStr);
//        Date endTime = sdfInMillSec.parse(endTimeStr);

//        ThreadLogContext entryContext = new ThreadLogContext(startTime, endTime, entryKeys);
//        ThreadIdAndTime tidAndStartTime = getEntryThreadIdAndTime(sdfInMillSec, new File[]{entryLog}, entryContext);


        LineContainer lineContainer = new LineContainer();

//        getAllLogsOfThreadId(tidAndStartTime, endTime, logFiles, lineContainer);

        File entryLog = new File(logFolder + File.separator + entryLogFileStr);
        File folder = new File(logFolder);
        File[] logFiles = getLogFiles(folder, fileBlackList);

        getStartLogs("2020-11-19 10:35:05,708", "2020-11-19 10:35:09,205",
                new String[]{"dataflowmanagement/probes/DataFlowProbe/ranges", "POST"},
                sdf, entryLog, logFiles, lineContainer);


        getLogs("2020-11-19 10:35:09,203", "2021-11-20 09:20:55,000",
                new String[]{"AutoDiscoveryOperationExecuteDomainScopeEvent@5c42fb77 begin to invoke"},
                sdf, logFiles, lineContainer);

        getLogs("2020-11-19 10:35:13,368", "2021-11-20 09:20:55,000",
                new String[]{"[{0}] start."},
                sdf, logFiles, lineContainer);

        getLogs("2020-11-19 10:35:15,209", "2021-11-20 09:20:55,000",
                new String[]{"- com.hp.ucmdb.discovery.server.utils.disptach.zone.UnDispatchOutOfZoneIpRangeTriggerAction@13e6e163"},
                sdf, logFiles, lineContainer);


        sort(lineContainer);

        saveToFile(lineContainer, new File(resultFileStr));

    }

    static void getStartLogs(String startTimeStr, String endTimeStr, String[] entryKeys, SimpleDateFormat sdf, File startLogFile, File[] logFiles, LineContainer lineContainer) throws Exception {
        Date startTime = sdf.parse(startTimeStr);
        Date endTime = sdf.parse(endTimeStr);

        ThreadLogContext entryContext = new ThreadLogContext(startTime, endTime, entryKeys);

        ThreadIdAndTime tidAndStartTime = getEntryThreadIdAndTime(sdf, new File[]{startLogFile}, entryContext);

        getAllLogsOfThreadId(tidAndStartTime, endTime, logFiles, lineContainer);
    }

    static void getLogs(String startTimeStr, String endTimeStr, String[] entryKeys, SimpleDateFormat sdf, File[] logFiles, LineContainer lineContainer) throws Exception {
        Date startTime = sdf.parse(startTimeStr);
        Date endTime = sdf.parse(endTimeStr);

        ThreadLogContext entryContext = new ThreadLogContext(startTime, endTime, entryKeys);

        ThreadIdAndTime tidAndStartTime = getEntryThreadIdAndTime(sdf, logFiles, entryContext);

        getAllLogsOfThreadId(tidAndStartTime, endTime, logFiles, lineContainer);
    }

    private static void saveToFile(LineContainer lineContainer, File file) throws IOException {
        List<LineData> datas = lineContainer.get();
        List<String> lines = new ArrayList<>();
        boolean logsAtSameTime = false;
        Calendar c = Calendar.getInstance();
        c.set(1970, -1 + 11, 0 + 5, 0 + 22, 0 + 0, 0 + 0);
        LineData preData = new LineData("", c.getTime(), "", 0);
        for (LineData data : datas) {
            if (0 == data.time.compareTo(preData.time)) {
                logsAtSameTime = true;
            } else if (logsAtSameTime){
                lines.add("");
                logsAtSameTime = false;
            }
            preData = data;
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
//                if (o1.line.contains("currentStatus is CONNECTED") || o2.line.contains("currentStatus is CONNECTED")) {
//                    System.out.println("hit");
//                }
                if (0 == c) {
//                    for (FileAndLineNum fl1 : o1.fileAndLineNums) {
//                        for (FileAndLineNum fl2 : o2.fileAndLineNums) {
//                            if (fl1.fileName.equals(fl2.fileName)) {
//                                System.out.println("c==0 fl1.lineNum: " + fl1.lineNum + ", fl2.lineNum: " + fl2.lineNum + ",        " + o1.line + " ----- " + o2.line);
//                                return fl2.lineNum - fl1.lineNum;
//                            }
//                        }
//                    }
                    if (o1.fileName.equals(o2.fileName)) {
//                        System.out.println("c==0 fl1.lineNum: " + o1.lineNum + ", fl2.lineNum: " + o2.lineNum + ",        " + o1.line + " ----- " + o2.line);
                        return o1.lineNum - o2.lineNum;
                    }
//                    System.out.println("c==0, not same file " + o1.line + " ----- " + o2.line);
                    return 0;
                } else {
                    // 时间不相等，按时间排序
//                    System.out.println(c + " " + o1.line + " ----- " + o2.line);
                    return c;
                }
            }
        });
    }

    private static void getAllLogsOfThreadId(ThreadIdAndTime tidAndTime, Date endTime, File[] logFiles, LineContainer lineContainer) throws ParseException {

        for (File logFile : logFiles) {
            List<String> lines = FileUtil.readTxtFileAsStrings(logFile);
            for (int i=0; i<lines.size(); i++) {
                String line = lines.get(i);
                Date time = null;
                try {
                    time = getTimeOfLog(line, sdfInMillSec);
                } catch (Exception e) {
//                    System.out.println("Parse log time fail on: " + line + ", file: " + logFile.getName() + ", line: " + i);
                    continue;
                }
                if (time.before(tidAndTime.time) || time.after(endTime)) {
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

    private static ThreadIdAndTime getEntryThreadIdAndTime(SimpleDateFormat sdf, File[] entryLogs, ThreadLogContext entryContext) throws Exception {
        Date currTime = null;
        ThreadIdAndTime curr = null;
        for (File entryLog : entryLogs) {
            List<String> lines = FileUtil.readTxtFileAsStrings(entryLog);
            for (String line : lines) {
                Date time = null;
                try {
                    time = getTimeOfLog(line, sdf);
                } catch (Exception e) {
//                    System.out.println("Parse log time fail on: " + line + ", file: " + logFile.getName() + ", line: " + i);
                    continue;
                }
                if (time.before(entryContext.startTime)) {
                    continue;
                }
                if (lineContainsAllKeys(line, entryContext.entryKeys)) {
                    if (null == currTime || time.before(currTime)) {
                        curr = new ThreadIdAndTime(getThreadId(line), time);
                        currTime = time;
                    }

                }
            }
        }
        if (null != curr) {
            return curr;
        } else {
            throw new Exception("Thread id not found by keys: " + entryContext.entryKeys[0] + " ...");
        }
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

    private static Date getTimeOfLog(String line, SimpleDateFormat sdf) throws ParseException {
        String[] tokens = line.split(" ");
        String timeStr = tokens[0] + " " + tokens[1];
        return sdf.parse(timeStr);
    }

}
