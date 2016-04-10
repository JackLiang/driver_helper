/**
 * 友魄科技
 */
package com.dh.common.utils;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * IP工具类
 * 
 * @author ying_liu 2015年4月16日
 *
 */
public class IpUtils {
	private static final Pattern IPV4_PATTERN = Pattern
			.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

	/**
	 * 
	 * 获取正确的客户端ip地址
	 * 
	 * @param request
	 * 
	 */
	public static String getClientIpAddr(HttpServletRequest request) {
		String ipAddress = null;
		ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if ("127.0.0.1".equals(ipAddress)
					|| "0:0:0:0:0:0:0:1".equals(ipAddress)) {
				// 本机配置的IP
				ipAddress = OSUtils.getLocalIp();
			}

		}
		if (ipAddress != null) {
			String[] ips = ipAddress.split(",");
			for (String ip : ips) {
				if (isIPv4Address(ip.trim())) {
					ipAddress = ip.trim();
					break;
				}
			}
		}

		return ipAddress;

	}

	/**
	 * 测试传入的参数是否正确的ipv4格式
	 * 
	 * @param ip
	 * 
	 */
	public static boolean isIPv4Address(String ip) {
		if (ip == null)
			return false;

		return IPV4_PATTERN.matcher(ip).matches();
	}

	public static void main(String[] args) {
		System.out.println(OSUtils.getLocalIp());
	}
}
