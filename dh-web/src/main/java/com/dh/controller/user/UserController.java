package com.dh.controller.user;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.dh.common.constant.SessionKey;
import com.dh.common.utils.UUIDUtils;
import com.dh.model.User;
import com.dh.service.user.UserService;
import com.dh.vo.RespVO;

@Controller
@RequestMapping("/user")
public class UserController {

	@Resource(name = "userService")
	private UserService userService;

	@RequestMapping("/authenticate.do")
	public Object authenticate(HttpServletRequest request, String account,
			String password) {

		if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
			return JSON.toJSONString(new RespVO(-1, "帐号或密码不能为空"));
		}

		User user = userService.authenticate(account, password);

		if (user == null) {
			return JSON.toJSONString(new RespVO(-2, "帐号或密码不正确"));
		}

		HttpSession session = request.getSession(true);
		session.setAttribute(SessionKey.DH_USER, user);

		return JSON.toJSONString(new RespVO(0, "登录成功"));
	}

	@RequestMapping("/regist.do")
	public Object regist(String account, String password) {

		// 生成8位userId
		String userId = UUIDUtils.getNumUUID(8);
		User user = new User();
		user.setAccount(account);
		user.setPassword(password);
		user.setPlatform(0);
		user.setCreateTime(new Date());
		user.setUserId(userId);
		boolean result = userService.regist(user);

		if (!result) {
			return JSON.toJSONString(new RespVO(-2, "注册失败"));
		}

		User entity = userService.getInfo(user.getUserId());

		return JSON.toJSONString(new RespVO(0, "注册成功", entity));
	}

	@RequestMapping("/getUserInfo.do")
	public Object getUserInfo(String userId) {

		if (StringUtils.isBlank(userId)) {
			return JSON.toJSONString(new RespVO(-1, "参数错误"));
		}

		User entity = userService.getInfo(userId);

		return JSON.toJSONString(new RespVO(0, "获取用户信息成功", entity));
	}

	
	public Object updateUser(HttpServletRequest request, @RequestParam(value = "player_id", required = true) String playerId,
			@RequestParam(value = "nick_name", required = false) String nickName, @RequestParam(value = "head_img", required = false) String headImg,
			@RequestParam(value = "phone", required = false) String phone, @RequestParam(value = "sex", required = false) Integer sex,
			@RequestParam(value = "birth_day", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthDay){
		
		
		
				return birthDay;
		
	}
	
}
