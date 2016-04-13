package com.dh.service.user;

import com.dh.model.User;

public interface UserService {

	
	/**帐号密码登录
	 * @param account
	 * @param password
	 * @return
	 */
	public User authenticate(String account, String password);
	
	/**
	 * 手机注册
	 * @param user
	 * @return
	 */
	public boolean regist(User user);
	
	/**
	 * 获取用户信息
	 * @param userId
	 * @return
	 */
	public User getInfo(String userId);
	
	/**
	 * 修改用户信息
	 * @param user
	 * @return
	 */
	public boolean update(User user);
	
	/**
	 * 第三方登录
	 * @param user
	 * @return
	 */
	public boolean registOther(User user);
	
	/**
	 * 获取第三方用户信息
	 * @param openId
	 * @return
	 */
	public User getByOpenId(String openId);
}
