package com.dh.dao.user;

import com.dh.model.User;

public interface UserDao {

	
	public int insert(User user);
	
	public int update(User user);
	
	public User get(String account,String password);
	
	public User get(String userId);
	
	public User getByOpenId(String openId);
}
