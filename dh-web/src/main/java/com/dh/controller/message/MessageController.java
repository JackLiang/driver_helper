package com.dh.controller.message;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dh.common.constant.SessionKey;
import com.dh.controller.user.UserController;
import com.dh.interceptor.annotation.CleanUserAgent;
import com.dh.model.Message;
import com.dh.model.User;
import com.dh.service.message.MessageService;
import com.dh.vo.RespVO;
import com.dh.vo.message.MessageCriteria;

@RequestMapping("/message")
@Controller
public class MessageController {

	@Resource(name = "messageService")
	private MessageService messageService;

	private final static Logger LOG = LoggerFactory.getLogger(UserController.class);

	@ResponseBody
	@RequestMapping("/addReport.do")
	@CleanUserAgent
	public Object addReport(HttpServletRequest request, String user_id, String title, Integer type, String desc,
			String imgs, String location, String address) {
		LOG.info("addReport:用户上报，user_id【{}】,title【{}】,type【{}】,imgs【{}】,location【{}】,address【{}】", user_id, title,
				type, imgs, location, address);
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(SessionKey.DH_USER);
		if (user == null || !user.getUserId().equals(user_id)) {
			LOG.error("addReport:用户非法，session的user_id【{}】,输入的user_id【{}】", user.getUserId(), user_id);
			return JSON.toJSONString(new RespVO(-1, "用户非法"));
		}
		if(StringUtils.isBlank(location)){
			return JSON.toJSONString(new RespVO(-2, "坐标不允许空"));
		}
		if(StringUtils.isBlank(imgs)){
			return JSON.toJSONString(new RespVO(-3, "最少上传一张图片"));
		}
		if(StringUtils.isBlank(title)){
			return JSON.toJSONString(new RespVO(-4, "消息主题不能为空"));
		}
		if(type <= 0){
			return JSON.toJSONString(new RespVO(-5, "消息类型错误"));
		}
		if(StringUtils.isBlank(address)){
			return JSON.toJSONString(new RespVO(-6, "上传区域不正确"));
		}
		Message msg = new Message();
		msg.setUserId(user.getId());
		msg.setTitle(title);
		msg.setType(1 << type);
		msg.setDescription(desc);
		msg.setImgs(imgs);
		msg.setLocation(location);
		msg.setAddress(address);
		msg.setIsUsed(0);
		msg.setNoUsed(0);
		msg.setCreateTime(new Date());

		int code = messageService.send(msg) ? 0 : -1;
		return JSON.toJSONString(new RespVO(code, "上报成功"));
	}

	@ResponseBody
	@RequestMapping("/getReport.do")
	@CleanUserAgent
	public Object getReport(HttpServletRequest request, String user_id,
			@RequestParam(value = "interest", required = true) int interest, String address, String location) {
		LOG.info("getReport:获取上报信息，user_id【{}】,interest【{}】,address【{}】", user_id, interest, address);
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(SessionKey.DH_USER);
		if (user == null || !user.getUserId().equals(user_id)) {
			LOG.error("addReport:用户非法，session的user_id【{}】,输入的user_id【{}】", user.getUserId(), user_id);
			return JSON.toJSONString(new RespVO(-1, "用户非法"));
		}
		if(StringUtils.isBlank(location)){
			return JSON.toJSONString(new RespVO(-2, "坐标不允许空"));
		}
		
		if(interest <= 0){
			return JSON.toJSONString(new RespVO(-3, "至少选择一个偏好"));
		}
		if(StringUtils.isBlank(address)){
			return JSON.toJSONString(new RespVO(-4, "查询区域不正确"));
		}
		MessageCriteria mc = new MessageCriteria();
		mc.setAddress(address);
		mc.setInterest(interest);
		mc.setLimit(10);
		mc.setLocation(location);
		List<Message> msgs = messageService.getByCriteria(mc);

		return JSON.toJSONString(new RespVO(0, "获取成功", msgs));

	}

	@ResponseBody
	@RequestMapping("/evaluate.do")
	public Object evaluate(HttpServletRequest request, String user_id, int msg_id, int type) {
		
		LOG.info("evaluate:评价,user_id【{}】,msg_id【{}】,type【{}】", user_id, msg_id, type);
		if(type < 1 || type >2){
			return JSON.toJSONString(new RespVO(-1, "评价类型只能是1或者2"));
		}
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(SessionKey.DH_USER);
		if (user == null || !user.getUserId().equals(user_id)) {
			LOG.error("addReport:用户非法，session的user_id【{}】,输入的user_id【{}】", user.getUserId(), user_id);
			return JSON.toJSON(new RespVO(-1, "用户非法"));
		}
		messageService.evaluate(msg_id, type);
		return JSON.toJSONString(new RespVO(0, "评价成功"));

	}

}
