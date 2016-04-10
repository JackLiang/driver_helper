/**
 * 友魄科技
 */
package com.dh.common.utils;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring context util 获得 Spring context 的 context
 * 
 * @author ying_liu 2015年4月16日
 * 
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

	private static ApplicationContext context = null;

	public static ApplicationContext getContext() {
		return context;
	}

	public static void setContext(ApplicationContext context) {
		SpringContextUtil.context = context;
	}

	public static Object getBean(String beanId) {
		try {
			return context.getBean(beanId);
		} catch (Exception e) {
			return null;
		}
	}
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		SpringContextUtil.context = context;
	}

	public static void autowireBeanPropertiesByName(Object existingBean) {
		getContext().getAutowireCapableBeanFactory().autowireBeanProperties(existingBean, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
	}

	public static void autowireBeanPropertiesByType(Object existingBean) {
		getContext().getAutowireCapableBeanFactory().autowireBeanProperties(existingBean, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
	}
	
	/**
	 * 获取项目绝对路径
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String getProjectPath() throws IOException {
		return context.getResource("").getFile().getAbsolutePath();
	}
}
