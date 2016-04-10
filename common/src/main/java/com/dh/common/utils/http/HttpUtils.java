/**
 * 友魄科技
 */
package com.dh.common.utils.http;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP工具类
 * 
 * @author ying_liu 2015年4月10日
 *
 */
public class HttpUtils {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpUtils.class);

	/**
	 * DefaultHttpClient 是线程安全的
	 */
	private static final HttpClient hc = createHttpClient();

	/**
	 * 创建一个 HttpClient
	 * 
	 * @return
	 */
	private static HttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();
		PoolingClientConnectionManager pcm = new PoolingClientConnectionManager();
		pcm.setDefaultMaxPerRoute(100); // 对每个指定连接的服务器（指定的ip）可以创建并发50 socket进行访问
		pcm.setMaxTotal(300); // 客户端总并行链接最大数
		// 设置timeout 时间
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 10000);
		return new DefaultHttpClient(pcm, params);
	}

	/**
	 * 这是组装http header
	 * 
	 * @param headers
	 * @return
	 */
	private static Header[] assemblyHeader(Map<String, String> headers) {
		Header[] allHeader = new BasicHeader[headers.size()];
		int i = 0;
		for (String str : headers.keySet()) {
			allHeader[i] = new BasicHeader(str, headers.get(str));
			i++;
		}
		return allHeader;
	}

	/**
	 * 组装参数
	 * 
	 * @param parameters
	 * @return
	 */
	private static List<NameValuePair> assemblyParameter(
			Map<String, String> parameters) {
		List<NameValuePair> list = new ArrayList<>(parameters.size());
		for (Entry<String, String> entry : parameters.entrySet()) {
			list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return list;
	}

	/**
	 * 获取URL的结果，编码是UTF-8
	 * 
	 * @param url
	 *            - URL
	 * @return 如果出错，抛出RuntimeException，否则返回不为null的经过trim()后的字符串
	 */
	public static String getUTF8(String url) {
		return doHttpGet(url, null, null, "UTF-8");
	}

	/**
	 * 获取URL的结果，编码是UTF-8
	 * 
	 * @param url
	 *            - URL
	 * @param parameters
	 *            - 参数，可为null
	 * @return 如果出错，抛出RuntimeException，否则返回不为null的经过trim()后的字符串
	 */
	public static String getUTF8(String url, Map<String, String> parameters) {
		return doHttpGet(url, null, parameters, "UTF-8");
	}

	/**
	 * 获取URL的结果，编码是UTF-8，如果获取结果为空，或出错，将继续重试，直到 maxTryTimes 为止
	 * 
	 * @param url
	 *            - URL
	 * @param parameters
	 *            - 参数，可为null
	 * @param maxTryTimes
	 *            - 最大重试次数
	 * @return 如果出错，返回""
	 */
	public static String getUTF8(String url, Map<String, String> parameters,
			int maxTryTimes) {
		return getUTF8(url, parameters, maxTryTimes, false);
	}

	/**
	 * 获取URL的结果，编码是UTF-8，如果获取结果为空，或出错，将继续重试，直到 maxTryTimes 为止
	 * 
	 * @param url
	 *            - URL
	 * @param parameters
	 *            - 参数，可为null
	 * @param maxTryTimes
	 *            - 最大重试次数
	 * @param throwEx
	 *            - 是否抛出异常
	 * @return 如果出错，返回""
	 */
	public static String getUTF8(String url, Map<String, String> parameters,
			int maxTryTimes, boolean throwEx) {
		if (maxTryTimes < 1 || maxTryTimes > 10) {
			maxTryTimes = 3;
		}
		RuntimeException ex = null;
		for (int i = 0; i < maxTryTimes; i++) {
			try {
				String str = doHttpGet(url, null, parameters, "UTF-8");
				if (StringUtils.isNotEmpty(str))
					return str;
			} catch (RuntimeException e) {
				ex = e;
			}
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e1) {
				// ignore
			}
		}
		if (throwEx) {
			throw ex;
		}
		return "";
	}

	/**
	 * 发送 GET 请求
	 * 
	 * @param url
	 *            - 地址
	 * @param headers
	 *            - http header
	 * @param parameters
	 *            - 参数列表
	 * @param encoding
	 *            - 编码，比如 UTF-8
	 * @return 如果出错，抛出RuntimeException，否则返回不为null的经过trim()后的字符串
	 */
	public static String doHttpGet(String url, Map<String, String> headers,
			Map<String, String> parameters, String encoding) {
		String body;
		try {
			// 设置参数
			if (parameters != null && parameters.size() > 0) {
				String str = EntityUtils.toString(new UrlEncodedFormEntity(
						assemblyParameter(parameters), encoding));
				if (url.lastIndexOf("?") > 0) {
					// 2015/3/31 by linyi，处理URL里有问号的情况
					if (url.endsWith("?")) {
						url = url + str;
					} else {
						url = url + "&" + str;
					}
				} else {
					url = url + "?" + str;
				}
			}
			// Get请求
			HttpGet httpget = new HttpGet(url);
			if (null != headers && headers.size() > 0) {
				httpget.setHeaders(assemblyHeader(headers));
			}
			long t1 = System.currentTimeMillis();
			// 发送请求
			HttpResponse httpResponse = hc.execute(httpget);
			// 获取返回数据
			HttpEntity entity = httpResponse.getEntity();
			body = EntityUtils.toString(entity, encoding);
			EntityUtils.consumeQuietly(entity);
			if (body != null) {
				body = body.trim();
			}
			long time = System.currentTimeMillis() - t1;
			logger.info("get url=" + url + ", time=" + time + " ms");
		} catch (ParseException e) {
			throw new RuntimeException("parse error for url:" + url, e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("bad encoding:" + encoding, e);
		} catch (IOException e) {
			throw new RuntimeException("error for url:" + url, e);
		}
		return body;
	}

	/**
	 * 获取图片请求
	 * 
	 * @param url
	 *            - 地址
	 * @param headers
	 *            - http header
	 * @param parameters
	 *            - 参数列表
	 * @param encoding
	 *            - 编码，比如 UTF-8
	 * @return 如果出错，抛出RuntimeException，否则返回不为null的经过trim()后的字符串
	 */
	public static BufferedImage getImage(String url, Map<String, String> headers,
			Map<String, String> parameters, String encoding) {
		BufferedImage image = null;
		try {
			// 设置参数
			if (parameters != null && parameters.size() > 0) {
				String str = EntityUtils.toString(new UrlEncodedFormEntity(
						assemblyParameter(parameters), encoding));
				if (url.lastIndexOf("?") > 0) {
					// 处理URL里有问号的情况
					if (url.endsWith("?")) {
						url = url + str;
					} else {
						url = url + "&" + str;
					}
				} else {
					url = url + "?" + str;
				}
			}
			// Get请求
			HttpGet httpget = new HttpGet(url);
			if (null != headers && headers.size() > 0) {
				httpget.setHeaders(assemblyHeader(headers));
			}
			long t1 = System.currentTimeMillis();
			// 发送请求
			HttpResponse httpResponse = hc.execute(httpget);
			// 获取返回数据
			HttpEntity entity = httpResponse.getEntity();
			// 将返回的输入流转换成字符串
			InputStream connIn = entity.getContent();
			image = ImageIO.read(connIn);
			EntityUtils.consumeQuietly(entity);
			long time = System.currentTimeMillis() - t1;
			logger.info("get url=" + url + ", time=" + time + " ms");
		} catch (ParseException e) {
			throw new RuntimeException("parse error for url:" + url, e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("bad encoding:" + encoding, e);
		} catch (IOException e) {
			throw new RuntimeException("error for url:" + url, e);
		}
		return image;
	}

	/**
	 * 将返回的输入流转换成字节数组
	 * 
	 * @param url
	 *            - 地址
	 * @param headers
	 *            - http header
	 * @param parameters
	 *            - 参数列表
	 * @param encoding
	 *            - 编码，比如 UTF-8
	 * @return 如果出错，抛出RuntimeException，否则返回不为null的经过trim()后的字符串
	 */
	public static byte[] getByteArray(String url, Map<String, String> headers,
			Map<String, String> parameters, String encoding) {
		try {
			// 设置参数
			if (parameters != null && parameters.size() > 0) {
				String str = EntityUtils.toString(new UrlEncodedFormEntity(
						assemblyParameter(parameters), encoding));
				if (url.lastIndexOf("?") > 0) {
					// 处理URL里有问号的情况
					if (url.endsWith("?")) {
						url = url + str;
					} else {
						url = url + "&" + str;
					}
				} else {
					url = url + "?" + str;
				}
			}
			// Get请求
			HttpGet httpget = new HttpGet(url);
			if (null != headers && headers.size() > 0) {
				httpget.setHeaders(assemblyHeader(headers));
			}
			long t1 = System.currentTimeMillis();
			// 发送请求
			HttpResponse httpResponse = hc.execute(httpget);
			// 获取返回数据
			HttpEntity entity = httpResponse.getEntity();
			// 将返回的输入流转换成字节数组
			InputStream connIn = entity.getContent();
			byte[] result = IOUtils.toByteArray(connIn);
			EntityUtils.consumeQuietly(entity);
			long time = System.currentTimeMillis() - t1;
			logger.info("get url=" + url + ", time=" + time + " ms");
			return result;
		} catch (ParseException e) {
			throw new RuntimeException("parse error for url:" + url, e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("bad encoding:" + encoding, e);
		} catch (IOException e) {
			throw new RuntimeException("error for url:" + url, e);
		}
	}

	/**
	 * 发送POST请求，使用UTF-8编码
	 * 
	 * @param url
	 *            - URL
	 * @param parameters
	 *            - 参数，可为null
	 * @return 如果出错，抛出RuntimeException，否则返回不为null的经过trim()后的字符串
	 */
	public static String postUTF8(String url, Map<String, String> parameters) {
		return doHttpPost(url, null, parameters, "UTF-8");
	}

	/**
	 * 发送POST请求
	 * 
	 * @param url
	 *            - URL
	 * @param headers
	 *            - HTTP header
	 * @param parameters
	 *            - 参数，可为null
	 * @param encoding
	 *            - 编码
	 * @return 如果出错，抛出RuntimeException，否则返回不为null的经过trim()后的字符串
	 */
	public static String doHttpPost(String url, Map<String, String> headers,
			Map<String, String> parameters, String encoding) {
		String body = null;
		HttpPost httpPost = new HttpPost(url);
		try {
			// Post请求
			if (null != headers && headers.size() > 0) {
				httpPost.setHeaders(assemblyHeader(headers));
			}
			// 设置参数
			if (parameters != null && parameters.size() > 0) {
				httpPost.setEntity(new UrlEncodedFormEntity(
						assemblyParameter(parameters), encoding));
			}
			long t1 = System.currentTimeMillis();
			// 发送请求
			HttpResponse httpresponse = hc.execute(httpPost);
			// 获取返回数据
			HttpEntity entity = httpresponse.getEntity();
			body = EntityUtils.toString(entity, encoding);
			EntityUtils.consumeQuietly(entity);
			long time = System.currentTimeMillis() - t1;
			logger.info("post url=" + url + ", time=" + time + " ms");
		} catch (ParseException e) {
			throw new RuntimeException("parse error for url:" + url, e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("bad encoding:" + encoding, e);
		} catch (IOException e) {
			throw new RuntimeException("error for url:" + url, e);
		} finally {
			httpPost.releaseConnection();
		}
		return body;
	}

	/**
	 * 发送POST请求
	 * 
	 * @param url
	 *            - URL
	 * @param headers
	 *            - HTTP header
	 * @param sendStr
	 *            - 发送的内容
	 * @param encoding
	 *            - 编码
	 * @return 如果出错，抛出RuntimeException，否则返回不为null的经过trim()后的字符串
	 */
	public static String doHttpPost(String url, Map<String, String> headers,
			String sendStr, String encoding) {
		String body = null;
		HttpPost httpPost = new HttpPost(url);
		try {
			// Post请求
			if (null != headers && headers.size() > 0) {
				httpPost.setHeaders(assemblyHeader(headers));
			}
			// 发送内容
			if (StringUtils.isNotBlank(sendStr)) {
				httpPost.setEntity(new StringEntity(sendStr, encoding));
			}
			long t1 = System.currentTimeMillis();
			// 发送请求
			HttpResponse httpresponse = hc.execute(httpPost);
			// 获取返回数据
			HttpEntity entity = httpresponse.getEntity();
			body = EntityUtils.toString(entity, encoding);
			EntityUtils.consumeQuietly(entity);
			long time = System.currentTimeMillis() - t1;
			logger.info("post url=" + url + ", sendStr=" + sendStr + ", time=" + time + " ms");
		} catch (ParseException e) {
			throw new RuntimeException("parse error for url:" + url, e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("bad encoding:" + encoding, e);
		} catch (IOException e) {
			throw new RuntimeException("error for url:" + url, e);
		} finally {
			httpPost.releaseConnection();
		}
		return body;
	}

	public static void main(String[] args) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("passport", "阿三LR");
		getUTF8("http://pay.duowan.com/userDepositDWAction.action", parameters);
		getUTF8("http://pay.duowan.com");
	}
}
