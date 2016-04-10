/**
 * 友魄科技
 */
package com.dh.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 常用工具类
 * 
 * @author haibin_chen 2015年4月9日
 *
 */
public class CommonUtils {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private static final SimpleDateFormat FORMAT2 = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 生成23位订单号，如: 20150409154025111123456
	 * 
	 * @return
	 */
	public static String generateOrderId() {
		Date now = new Date();
		String prefix = FORMAT.format(now);
		String suffix = randomNumString(6);
		return prefix + suffix;
	}
	
	public static String randomNumString(int length) {
		String str = "0123456789";
		int size = str.length();
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int num = random.nextInt(size);
			buf.append(str.charAt(num));
		}
		return buf.toString();
	}
	
	/** 
	 * 产生一个随机的字符串，只包含数字
	 * 
	 * @param length
	 * @return
	 */
	public static String randomStringWithoutNum(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int size = str.length();
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int num = random.nextInt(size);
			buf.append(str.charAt(num));
		}
		return buf.toString();
	}
	
	/** 
	 * 产生一个随机的字符串
	 * 
	 * @param length 当length等于9的时候，测到OOM都没发现重复，8位能保证一千万次不重复
	 * @return
	 */
	public static String randomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		int size = str.length();
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int num = random.nextInt(size);
			buf.append(str.charAt(num));
		}
		return buf.toString();
	}
	/**
	 * 获取去除一些特别字母的数，比如g,q,p
	 * @param length
	 * @return
	 */
	public static String randomStringSpecial(int length){
		String str = "abcdefhiklmnorstuvwxzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		int size = str.length();
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int num = random.nextInt(size);
			buf.append(str.charAt(num));
		}
		return buf.toString();
	}
	
	/**
	 * 返回时间字符串，格式：yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToStr(long date) {
		return FORMAT2.format(new Date(date));
	}
	
	/**
	 * 返回时间字符串，格式：yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToStr(Date date) {
		if (date == null) {
			// Not serious
			return "";
		}
		return FORMAT2.format(date);
	}
	
	/**此类的内部测试*/
	private static Map<String, String> INNER_TEST_MAP = new HashMap<>();
	
	/**
	 * 测试随机字符串的重复概率
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		//8位可以保证1千万次无重复
		for(int i = 0; i< 20000000;i++) {
//			String key = randomUuidStr();
			String key = randomString(9);
//			String key = randomStringWithoutNum(8);
			if(INNER_TEST_MAP.containsKey(key)) {
				System.out.println("break:" + i);
				System.out.println(key);
				System.exit(0);
			} else {
				INNER_TEST_MAP.put(key,"1");
			}
		}
		System.out.println("success:" + INNER_TEST_MAP.size());
		System.out.println(INNER_TEST_MAP.entrySet().iterator().next().getKey());
	}
}
