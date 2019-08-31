package org.danny.tools.common.net;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;

public class UrlDownloader {
	
	public static String downloadHtml(String url) throws HttpException, IOException {
		ByteArrayOutputStream baos = download(url);
		if (null != baos) {
			return baos.toString();
		} else {
			return "";
		}		
	}
	
	public static byte[] downloadBytes(String url) throws HttpException, IOException {
		ByteArrayOutputStream baos = download(url);
		if (null != baos) {
			return baos.toByteArray();
		} else {
			return new byte[0];
		}
	}
	
	protected static ByteArrayOutputStream download(String url) throws HttpException, IOException {
		BufferedInputStream bis = null;
		GetMethod getMethod = null;   
		ByteArrayOutputStream baos = null;
        try {
            URI uri = new URI(url,false, "GBK");
            HttpClient hc = new HttpClient();
            hc.setConnectionTimeout(30000);
            hc.setTimeout(30000);
            getMethod = new GetMethod(uri.toString());   
            int status = hc.executeMethod(getMethod);   
            if (status == 200) {   
                bis = new BufferedInputStream(getMethod.getResponseBodyAsStream());   
                baos = new ByteArrayOutputStream();
  
                byte[] buffer = new byte[1024];   
                int len = 0;   
                while ((len = bis.read(buffer)) != -1) {   
                	baos.write(buffer, 0, len);
                }   
            }   
        } catch (Exception e) {
        	System.out.println("下载失败，url: " + url);
        } finally {   
            if(getMethod != null){   
                getMethod.releaseConnection();   
            }   
            if(bis != null){   
                bis.close();   
            }   
        }
        return baos;
	}
}
