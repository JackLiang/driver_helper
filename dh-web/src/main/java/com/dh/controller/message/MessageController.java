package com.dh.controller.message;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSON;
import com.dh.common.constant.SessionKey;
import com.dh.controller.user.UserController;
import com.dh.model.Message;
import com.dh.model.User;
import com.dh.service.message.MessageService;
import com.dh.vo.RespVO;
import com.dh.vo.message.MessageCriteria;

@RequestMapping("/message")
@Controller
@SessionAttributes({ SessionKey.DH_USER })
public class MessageController {

	@Resource(name = "messageService")
	private MessageService messageService;

	private final static Logger LOG = LoggerFactory.getLogger(UserController.class);

	@ResponseBody
	@RequestMapping("/addReport.do")
	public Object addReport(@ModelAttribute(SessionKey.DH_USER) User user, String user_id, String title, int type,
			String desc, String imgs, String location, String address) {
		LOG.info("addReport:用户上报，user_id【{}】,title【{}】,type【{}】,imgs【{}】,location【{}】,address【{}】", user_id, title,
				type, imgs, location, address);
		if (!user.getUserId().equals(user_id)) {
			LOG.error("addReport:用户非法，session的user_id【{}】,输入的user_id【{}】", user.getUserId(), user_id);
			return JSON.toJSON(new RespVO(-1, "用户非法"));
		}
		Message msg = new Message();
		msg.setUserId(user.getId());
		msg.setTitle(title);
		msg.setType(1 << type);
		msg.setDesc(desc);
		msg.setImgs(imgs);
		msg.setLocation(location);
		msg.setAddress(address);
		msg.setCreateTime(new Date());

		int code = messageService.send(msg) ? 0 : -1;
		return JSON.toJSON(new RespVO(code, "上报成功"));
	}

	@ResponseBody
	@RequestMapping("/getReport.do")
	public Object getReport(@ModelAttribute(SessionKey.DH_USER) User user, String user_id, int interest, String address,String location) {
		LOG.info("getReport:获取上报信息，user_id【{}】,interest【{}】,address【{}】", user_id, interest, address);
		if (!user.getUserId().equals(user_id)) {
			LOG.error("addReport:用户非法，session的user_id【{}】,输入的user_id【{}】", user.getUserId(), user_id);
			return JSON.toJSON(new RespVO(-1, "用户非法"));
		}
		MessageCriteria mc = new MessageCriteria();
		mc.setAddress(address);
		mc.setInterest(interest);
		mc.setLimit(10);
		mc.setLocation(location);
		List<Message> msgs = messageService.getByCriteria(mc);

		return JSON.toJSON(new RespVO(0, "上报成功", msgs));

	}

}
