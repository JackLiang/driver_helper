package com.dh.controller.user;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

	private final static Logger LOG = LoggerFactory.getLogger(UserController.class);

	@RequestMapping("/authenticate.do")
	@ResponseBody
	public Object authenticate(HttpServletRequest request, String account, String password) {

		LOG.info("authenticate：手机登录开始,account【{}】,password【{}】", account, password);
		if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
			return JSON.toJSONString(new RespVO(-1, "帐号或密码不能为空"));
		}

		User user = userService.authenticate(account, password);

		if (user == null) {
			return JSON.toJSONString(new RespVO(-2, "帐号或密码不正确"));
		}

		HttpSession session = request.getSession(true);
		session.setAttribute(SessionKey.DH_USER, user);

		return JSON.toJSONString(new RespVO(0, "登录成功", user.getUserId()));
	}

	@RequestMapping("/regist.do")
	@ResponseBody
	public Object regist(String account, String password) {

		LOG.info("regist:手机注册开始,account【{}】,password【{}】", account, password);
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
			LOG.error("regist:手机注册失败,account【{}】,password【{}】,user_id【{}】", account, password, userId);
			return JSON.toJSONString(new RespVO(-2, "注册失败"));
		}

		User entity = userService.getInfo(user.getUserId());
		LOG.info("regist:手机注册成功，返回【{}】", JSON.toJSONString(new RespVO(0, "注册成功", entity)));
		return JSON.toJSONString(new RespVO(0, "注册成功", entity));
	}

	@RequestMapping("/registOther.do")
	@ResponseBody
	public Object registOther(HttpServletRequest request, String open_id, int platform) {

		LOG.info("registOther:第三方登录,open_id【{}】,platform【{}】", open_id, platform);
		User user = userService.getByOpenId(open_id);

		if (user == null) {
			String userId = UUIDUtils.getNumUUID(8);
			user = new User();
			user.setPlatform(0);
			user.setCreateTime(new Date());
			user.setUserId(userId);
			boolean result = userService.regist(user);
			if (!result) {
				LOG.error("registOther:第三方注册失败,open_id【{}】,platform【{}】,user_id【{}】", open_id, platform, userId);
				return JSON.toJSONString(new RespVO(-2, "第三方注册失败"));
			}

			LOG.info("registOther:第三方注册成功,open_id【{}】,platform【{}】,user_id【{}】", open_id, platform, userId);
		}

		HttpSession session = request.getSession(true);
		session.setAttribute(SessionKey.DH_USER, user);
		return JSON.toJSONString(new RespVO(0, "第三方登录成功", user));

	}

	@RequestMapping("/getUserInfo.do")
	@ResponseBody
	public Object getUserInfo(String user_id) {
		LOG.info("getUserInfo：获取用户信息开始,user_id【{}】", user_id);
		if (StringUtils.isBlank(user_id)) {
			return JSON.toJSONString(new RespVO(-1, "参数错误"));
		}

		User entity = userService.getInfo(user_id);
		LOG.info("getUserInfo：获取用户信息,返回【{}】", JSON.toJSONString(new RespVO(0, "获取用户信息成功", entity)));
		return JSON.toJSONString(new RespVO(0, "获取用户信息成功", entity));
	}

	@RequestMapping("/updateUser.do")
	@ResponseBody
	public Object updateUser(
			HttpServletRequest request,
			@RequestParam(value = "user_id", required = true) String userId,
			@RequestParam(value = "nickname", required = false) String nickname,
			@RequestParam(value = "head_img", required = false) String headImg,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "sex", required = false) Integer sex,
			@RequestParam(value = "birthday", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthDay,
			@RequestParam(value = "license_time", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date licenseTime) {
		LOG.info(
				"updateUser:修改用户信息,user_id【{}】,nickname【{}】,head_img【{}】,phone【{}】,sex【{}】,birthday【{}】,license_time【{}】",
				userId, nickname, headImg, phone, sex, birthDay, licenseTime);
		User user = new User();
		user.setUserId(userId);
		user.setNickname(nickname);
		user.setHeadImg(headImg);
		user.setPhone(phone);
		user.setSex(sex);
		user.setBirthDay(birthDay);
		user.setLicenseTime(licenseTime);

		int code = userService.update(user) ? 0 : -1;

		return JSON.toJSONString(new RespVO(code, "修改用户信息"));

	}

}
