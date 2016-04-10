package com.dh.common.utils.http;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 处理HTTPS请求工具类
 * 
 * @author ying_liu 2015年4月11日
 * 
 */
public class HttpsUtils {

	/**
	 * 使用微信证书，发起HTTPS请求
	 * 
	 * @param requestUrl
	 * @param requestMethod
	 * @param outputStr
	 * @param certPath 证书路径
	 * @param certPassword 证书密码，默认是微信商户号
	 * @return
	 * @throws Exception
	 */
	public static String request(String requestUrl, String requestMethod,
			String outputStr, String certPath, String certPassword) throws Exception {
		String result = "";
		KeyStore keyStore  = KeyStore.getInstance("PKCS12");
		// 证书位置
        FileInputStream instream = new FileInputStream(certPath);
        
    	// 密码默认是MCHID
        char[] passwordChar = certPassword.toCharArray();
        try {
            keyStore.load(instream, passwordChar);
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, passwordChar)
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
        	CloseableHttpResponse response = null;
        	if (requestMethod.equalsIgnoreCase("GET")) {
        		HttpGet httpget = new HttpGet(requestUrl);
        		response = httpclient.execute(httpget);
        	} else {
            	HttpPost httpost = new HttpPost(requestUrl); 
            	httpost.addHeader("Connection", "keep-alive");
            	httpost.addHeader("Accept", "*/*");
            	httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            	httpost.addHeader("Host", "api.mch.weixin.qq.com");
            	httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            	httpost.addHeader("Cache-Control", "max-age=0");
            	httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
        		httpost.setEntity(new StringEntity(outputStr, "UTF-8"));
        		response = httpclient.execute(httpost);
        	}
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                	result = EntityUtils.toString(entity, "utf-8");
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return result;
	}

}
