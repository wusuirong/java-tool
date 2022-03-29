/*
 * MicroFocus.com Inc.
 * Copyright(c) 2020 All Rights Reserved.
 */
package org.danny.tools.tmp;

import org.danny.tools.common.io.FileUtil;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wusui
 * @version $Id: Base.java, 2020-11-05 4:22 PM wusui Exp $
 */
public class Base {

    public static final String TIME_FORMAT_MIL = "yyyy-MM-dd HH:mm:ss,SSS";
    static SimpleDateFormat sdfInMillSec = new SimpleDateFormat(TIME_FORMAT_MIL);

    public static final String TIME_FORMAT_SEC = "yyyy-MM-dd HH:mm:ss";
    static SimpleDateFormat sdfSec = new SimpleDateFormat(TIME_FORMAT_SEC);

    public static final String TIME_FORMAT_IN_COMMLOG = "yyyy/MM/dd HH:mm:ss";
    static SimpleDateFormat sdfInCommlog = new SimpleDateFormat(TIME_FORMAT_IN_COMMLOG);

    static Pattern patternOfSdfInMillSec = Pattern.compile("\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}:\\d{2}:\\d{2}\\,\\d{3}");

    static Date getTimeOfServerLog(String line) throws ParseException {
        try {
            String[] tokens = line.split("  ");
            if (tokens.length < 1) {
                return sdfInMillSec.parse("1900-01-01 00:00:00,000");
            }
            Matcher matcher = patternOfSdfInMillSec.matcher(tokens[0]);
            if (matcher.matches()) {
                return sdfInMillSec.parse(tokens[0]);
            }
            return sdfInMillSec.parse("1900-01-01 00:00:00,000");
        } catch (Exception e) {
            System.out.println("Fail in parsing: " + line);
            throw e;
        }

    }

    static Date getTimeOfLog(String line) throws ParseException {
        try {
            String[] tokens = line.split(" ");
            if (tokens.length < 2) {
                return sdfInMillSec.parse("1900-01-01 00:00:00,000");
            }
            if (tokens[0].startsWith("<")) {
                tokens[0] = tokens[0].substring(1);
            }
            if (tokens[1].endsWith(">")) {
                tokens[1] = tokens[1].substring(0, tokens[1].length() - 1);
            }
            String timeStr = tokens[0] + " " + tokens[1];
            return sdfInMillSec.parse(timeStr);
        } catch (Exception e) {
            System.out.println("Fail in parsing: " + line);
            throw e;
        }

    }

    static void copyFiles(String sourceDirName, String outputDirName, List<String> fileNames) {
        File sourceDir = new File(sourceDirName);
        File[] allFiles = sourceDir.listFiles();
        for (File sourceFile : allFiles) {
            String sourceFileName = sourceFile.getName();
            for (String fileName : fileNames) {
                if (sourceFileName.equals(fileName)) {
                    File destFile = new File(outputDirName + File.separator + sourceFileName);
                    FileUtil.copyFile(sourceFile.getAbsolutePath(), destFile.getAbsolutePath());
                }
            }
        }
    }

    public static void save(List<String> lines, File file, boolean append) throws IOException {
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;

        try {
            bos = new BufferedOutputStream(new FileOutputStream(file, append));

            for (String line : lines) {
                line = line + System.lineSeparator();
                byte[] bytes = line.getBytes();
                bis = new BufferedInputStream(new ByteArrayInputStream(bytes));
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
            }
        } finally {
            if (null != bos) {
                bos.close();
            }

            if (null != bis) {
                bis.close();
            }
        }

    }

    public static void save(String content, File file, boolean append) throws IOException {
        byte[] bytes = content.getBytes();
        save(bytes, file, append);
    }

    public static void save(byte[] content, File file, boolean append) throws IOException {
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;

        try {
            bos = new BufferedOutputStream(new FileOutputStream(file, append));
            bis = new BufferedInputStream(new ByteArrayInputStream(content));
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
        } finally {
            if (null != bos) {
                bos.close();
            }
            if (null != bis) {
                bis.close();
            }
        }

    }

    public static String readLastLine(File file, String charset) throws IOException {
        if (!file.exists() || file.isDirectory() || !file.canRead())
            return null;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
            long len = raf.length();
            if (len == 0L)
                return "";
            long pos = len - 1;
            while (pos > 0) {
                pos--;
                raf.seek(pos);
                if (raf.readByte() == '\n')
                    break;
            }
            if (pos == 0)
                raf.seek(0);

            byte[] bytes = new byte[(int) (len - pos)];
            raf.read(bytes);

            if (charset == null)
                return new String(bytes);

            return new String(bytes, charset);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (Exception e2) {
                }
            }
        }
        return null;
    }

}
