package com.dh.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.dh.common.constant.SessionKey;
import com.dh.vo.RespVO;

@Component("sessionInterceptor")
public class SessionInterceptor extends HandlerInterceptorAdapter{
	/**
	 * 判断session是否存在或过期
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SessionInterceptor.class);
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		LOG.debug("SessionInterceptor-handler:"+handler);
		SessionRequest path=((HandlerMethod) handler).getMethodAnnotation(SessionRequest.class);
//		Player player=new Player();
//		player.setId(1);
//		player.setNickName("Jack");
//		player.setPlayerId("02015945");
//		player.setAccount("jacktest");
//		player.setHeadImg("http://resource.uwan99.com/images/fenxiao/200/productImgThumb/pen.png");
//		player.setPhone("150233556455");
//		player.setEmail("lj3happy@163.com");
//		player.setSex(1);
//		player.setBalance(new BigDecimal(28028));
//		request.getSession().setAttribute(SessionKey.BAO_PLAYER,player);
		try {
			if(path!=null){
				if(request.getSession().getAttribute(SessionKey.DH_USER)==null){
					LOG.info("SessionInterceptor-session is null:" +handler);
					RespVO respVO = new RespVO(-51, "登录失效，请重新登录");
					String respStr = JSON.toJSONString(respVO);
					response.setCharacterEncoding("utf-8");
					response.setContentType("text/plain;charset=UTF-8");
					response.getWriter().write(respStr, 0, respStr.length());
					response.getWriter().flush();
					return false;
				}
			}
		} catch (Exception e) {
			LOG.error("SessionInterceptor-session error", e);
			return false;
		}
		return true;
	}
}
