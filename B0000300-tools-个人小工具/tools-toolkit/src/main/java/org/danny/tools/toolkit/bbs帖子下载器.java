package org.danny.tools.toolkit;

import org.apache.commons.httpclient.HttpException;
import org.danny.tools.common.io.FileUtil;
import org.danny.tools.common.net.UrlDownloader;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.commons.httpclient.HttpException;
//import org.danny.tools.common.io.FileUtil;
//import org.danny.tools.common.net.UrlDownloader;

/**
 * bbs帖子下载器
 *
 * @author Administrator
 */
public class bbs帖子下载器 {

    String urlPreffix = "http://www.bbs.net/";

    public static void main(String[] args) throws HttpException, IOException {
        bbs帖子下载器 dl = new bbs帖子下载器();
        dl.saveHtmls();
        dl.saveTorrents();
        /*
         * 1 读取列表页面内容
         * 2 分析列表中主题的标题和url
         * 3 根据主题的url下载内容
         * 4 保存内容到磁盘
         *
         * 1 读取磁盘页面
         * 2 分析页面中的torrent
         * 3 下载torrent
         * 4 保存torrent
         */
        System.out.println("执行完毕");
    }

    public void saveHtmls() throws HttpException, IOException {
        List<TopicInfo> topics = new ArrayList<TopicInfo>();

        topics = getTopicsInfoInListPage("http://www.bbs.net/index.php?sort_id=1&page=", 1, 7);
        for (TopicInfo ti : topics) {
            saveTopicContentToDisk(new File("E:\\temp_workspace\\tmp\\torrents\\亚洲\\"), urlPreffix, ti);
        }

        topics = getTopicsInfoInListPage("http://www.bbs.net/index.php?sort_id=4&page=", 1, 30);
        for (TopicInfo ti : topics) {
            saveTopicContentToDisk(new File("E:\\temp_workspace\\tmp\\torrents\\中国\\"), urlPreffix, ti);
        }

        topics = getTopicsInfoInListPage("http://www.bbs.net/index.php?sort_id=5&page=", 1, 61);
        for (TopicInfo ti : topics) {
            saveTopicContentToDisk(new File("E:\\temp_workspace\\tmp\\torrents\\日韩\\"), urlPreffix, ti);
        }
    }

    public void saveTorrents() throws IOException {
        saveTorrentInHtmlFiles(new File("E:\\temp_workspace\\tmp\\torrents\\亚洲\\"));
        saveTorrentInHtmlFiles(new File("E:\\temp_workspace\\tmp\\torrents\\中国\\"));
        saveTorrentInHtmlFiles(new File("E:\\temp_workspace\\tmp\\torrents\\日韩\\"));
    }

    public void saveTorrentInHtmlFiles(File dir) throws IOException {
        File[] files = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                if (file.getAbsolutePath().endsWith(".html")) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            saveTorrentInHtmlFile(dir, file, urlPreffix);
        }
    }

    public void saveTorrentInHtmlFile(File saveRoot, File htmlFile, String urlPreffix) throws IOException {
        String fileName = htmlFile.getAbsolutePath();
        fileName = fileName.replace(".html", ".torrent");
        File torrentFile = new File(fileName);
        if (torrentFile.exists()) {
            return;
        }

        String pageSrc = FileUtil.readFileAsString(htmlFile);

        //创建欲匹配并抽取的子串的正则表达式
        String regex = "<a.*点击此处下载种子";
        //调用Pattern类的工厂方法compile()得到Pattern实例,并将正则表达式传递给compile();
        Pattern pat = Pattern.compile(regex);
        //调用Pattern对象的matcher()方法,并将目标字符串作为参数传递给它,得到一个Matcher实例;
        Matcher mat = pat.matcher(pageSrc);
        //调用find(),查找匹配.如果存在匹配的序列,则返回true,否则返回false;如果find()成功,则调用group(),以获得匹配子串;
        if (mat.find()) {
            String str = mat.group();
            int start = str.indexOf("<a href=\"") + 9;
            int end = str.indexOf("\">");
            if (0 >= end) {
                return;
            }
            str = str.substring(start, end);
            byte[] bytes;
            try {
                System.out.print("下载");
                bytes = UrlDownloader.downloadBytes(urlPreffix + str);
                FileUtil.save(bytes, torrentFile);
                System.out.println("               下载完毕");
            } catch (Exception e) {
                System.out.println("               下载失败: " + htmlFile.getAbsolutePath());
            }
        }
    }

    /**
     * 1 读取列表页面内容，得到title和url
     *
     * @param urlPrefix
     * @param start
     * @param end
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public List<TopicInfo> getTopicsInfoInListPage(String urlPrefix, int start, int end) throws HttpException, IOException {
        List<TopicInfo> result = new ArrayList<TopicInfo>();
        for (int i = start; i < end + 1; i++) {
            String url = urlPrefix + i;
            System.out.println(url);
            List<TopicInfo> tis = this.downloadAllTopicsInfoInListPage(url);
            result.addAll(tis);
        }
        return result;
    }

    public String getHtmlFileName(File dir, TopicInfo ti) {
        return dir.getAbsolutePath() + File.separator + ti.title + ".html";
    }

    public String getTorrentFileName(File dir, TopicInfo ti) {
        return dir.getAbsolutePath() + File.separator + ti.title + ".torrent";
    }

    /**
     * 根据主题的url下载内容，如果磁盘已存在内容则忽略
     *
     * @param dir
     * @param ti
     * @throws IOException
     * @throws HttpException
     */
    public void saveTopicContentToDisk(File dir, String urlPrefix, TopicInfo ti) throws HttpException, IOException {
        String fileName = getHtmlFileName(dir, ti);
        File file = new File(fileName);
        if (file.exists()) {
            return;
        }
        String pageSrc = UrlDownloader.downloadHtml(urlPrefix + ti.url);
        ti.htmlSrc = pageSrc;
        FileUtil.save(ti.htmlSrc, file);
    }
	

	
/*	public void saveTopicHtmlsInListPage(File dir, String urlPrefix, int start, int end) throws HttpException, IOException {
		dir.mkdirs();
		for (int i=start; i<end+1; i++) {
			String url = urlPrefix + i;
			System.out.println(url);
			List<TopicInfo> tis = this.downloadAllTopicsInfoInListPage(url);
			
			for (TopicInfo ti : tis) {
				saveTopicHtml(ti, new File(dir.getAbsolutePath() + File.separator + ti.title + ".html"));
			}			
		}
	}*/
	
/*	public void saveTopicHtml(TopicInfo ti, File file) throws IOException {
		FileUtil.save(ti.htmlSrc, file);
	}*/

    public List<TopicInfo> downloadAllTopicsInfoInListPage(String listPageUrl) throws HttpException, IOException {
        String pageSrc = UrlDownloader.downloadHtml(listPageUrl);

        int start = pageSrc.indexOf("<tbody class=\"tbody\"");
        int end = pageSrc.indexOf("</tbody>");

        if (0 < start && start < end) {
            pageSrc = pageSrc.substring(start, end);
        } else {
            System.out.println("error listPage: " + listPageUrl);
        }
        return getTopicsInfo(pageSrc);
    }

    public List<TopicInfo> getTopicsInfo(String htmlSrc) {
        List<TopicInfo> tis = new ArrayList<TopicInfo>();

        String[] tds = htmlSrc.split("<td style");
        for (String td : tds) {
            td = td.replaceAll("[\r\n]", "");
            int start = td.indexOf("<a href=\"");
            if (0 >= start) {
                continue;
            }
            start += 9;
            int end = td.indexOf("\"", start);
            if (0 >= end) {
                continue;
            }
            String url = td.substring(start, end);

            start = td.indexOf("\"_blank\">", end);
            if (0 >= start) {
                continue;
            }
            start += 9;
            end = td.indexOf("</a>", start);
            if (0 >= end) {
                continue;
            }
            String title = td.substring(start, end);
            title = title.trim();
            title = title.replaceAll("[/:?\"<>|\\\\\\*]", "");
            TopicInfo ti = new TopicInfo(title, url);
            tis.add(ti);
        }
        return tis;
    }
	
/*	public List<TopicInfo> getTopicContents(List<TopicInfo> tis, String urlPreffix, String rootDir) throws HttpException, IOException {
		File dir = new File(rootDir);
		for (TopicInfo ti : tis) {
			File f = new File(dir.getAbsolutePath() + "\\" + ti.title + ".torrent");
			if (f.exists()) {
				continue;
			}
			String url = urlPreffix + ti.url;
			String pageSrc = UrlDownloader.downloadHtml(url);
			
			//创建欲匹配并抽取的子串的正则表达式
			String regex = "<a.*点击此处下载种子";
			//调用Pattern类的工厂方法compile()得到Pattern实例,并将正则表达式传递给compile();
			Pattern pat = Pattern.compile(regex);
			//调用Pattern对象的matcher()方法,并将目标字符串作为参数传递给它,得到一个Matcher实例;
			Matcher mat = pat.matcher(pageSrc);
			//调用find(),查找匹配.如果存在匹配的序列,则返回true,否则返回false;如果find()成功,则调用group(),以获得匹配子串;
			if (mat.find()) {
				String str = mat.group();
				int start = str.indexOf("<a href=\"") + 9;
				int end = str.indexOf("\">");
				if (0 >= end) {
					continue;
				}
				str = str.substring(start, end);
				byte[] bytes;
				try {
					System.out.print("下载");
					bytes = UrlDownloader.downloadBytes(urlPreffix + str);
					ti.torrent = bytes;
					saveTorrentToDisk(ti, f);
					System.out.println("               下载完毕");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (Exception e) {
					System.out.println("               下载失败: " + ti.title);
				}
			}
		}
		return tis;
	}*/

//	public void saveTorrentToDisk(TopicInfo ti, File file) throws IOException {
//		if (null == ti.torrent) {
//			return;
//		}
//		FileUtil.save(ti.torrent, file);
//	}

    class TopicInfo {
        String title;
        String url;
        String htmlSrc;
        byte[] torrent;

        public TopicInfo(String title, String url) {
            this.title = title;
            this.url = url;
        }
    }
}
