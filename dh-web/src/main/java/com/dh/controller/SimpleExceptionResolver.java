/**
 * 友魄科技
 */
package com.dh.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.dh.vo.RespVO;


/**
 * 处理Controller的一些非业务异常，如NullPointerException等
 * 
 * @author haibin_chen 2016年2月16日
 *
 */
public class SimpleExceptionResolver implements HandlerExceptionResolver {
	
	private static final Logger LOG = LoggerFactory.getLogger(SimpleExceptionResolver.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception e) {
		LOG.error("Controller发生异常", e);
		
		RespVO respVO = new RespVO(-1, "未知错误");
		String respStr = JSON.toJSONString(respVO);
		
		try {
			arg1.setCharacterEncoding("utf-8");
			arg1.setContentType("text/plain;charset=UTF-8");
			arg1.getWriter().write(respStr, 0, respStr.length());
			arg1.getWriter().flush();
		} catch (Exception e1) {
			LOG.error("Controller异常后处理失败", e1);
		}
		
		return null;
	}

}
