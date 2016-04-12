package com.dh.service.user;

import com.dh.model.User;

public interface UserService {

	
	public User authenticate(String account, String password);
	
	public boolean regist(User user);
	
	public User getInfo(String userId);
	
	public boolean update(User user);
}
