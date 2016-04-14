package com.dh.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dh.interceptor.annotation.CleanUserAgent;

@Component("appInterceptor")
public class APPInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOG = LoggerFactory.getLogger(APPInterceptor.class);


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String userAgent = request.getHeader("User-Agent");

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		CleanUserAgent cleanAll = method.getAnnotation(CleanUserAgent.class);

//		SystemProperty isTest = systemPropertiesDao.get("is_test");
//		if (isTest != null && isTest.getValue().equalsIgnoreCase("true")) {
//			return true;
//		}

		if (cleanAll != null) {
			return true;
		}
		if (userAgent != null && userAgent.toUpperCase().contains("ANDROID-DH")) {
			LOG.debug("andorid");
			return true;
		}
		if (userAgent != null && userAgent.toUpperCase().contains("IOS-DH")) {
			LOG.debug("ios");
			return true;
		}
		LOG.info("app:请求非app来源，请求失败.userAgent:" + userAgent);
		// 接入了H5页面，这里不做控制 TODO

		return true;
	}
}
