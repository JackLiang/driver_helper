package com.dh.service.user.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dh.dao.user.UserDao;
import com.dh.model.User;
import com.dh.service.user.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource(name = "userDao")
	private UserDao userDao;

	@Override
	public User authenticate(String account, String password) {

		return userDao.get(account, password);
	}

	@Override
	public boolean regist(User user) {
		return userDao.insert(user) > 0 ? true : false;
	}

	@Override
	public User getInfo(String userId) {
		return userDao.get(userId);
	}

	@Override
	public boolean update(User user) {
		return userDao.update(user) > 0 ? true : false;
	}

}
