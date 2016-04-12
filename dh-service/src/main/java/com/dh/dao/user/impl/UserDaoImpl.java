package com.dh.dao.user.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dh.common.BaseDao;
import com.dh.dao.mapper.UserMapper;
import com.dh.dao.user.UserDao;
import com.dh.model.User;

@Repository("userDao")
public class UserDaoImpl extends BaseDao implements UserDao{

	@Override
	public int insert(User user) {
		return this.getMapper(UserMapper.class).insertSelective(user);
	}

	@Override
	public int update(User user) {
		return this.getMapper(UserMapper.class).updateByPrimaryKeySelective(user);
	}

	@Override
	public User get(String account, String password) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("account", account);
		map.put("password", password);
		return this.getMapper(UserMapper.class).selectByAccountAndPassword(map);
	}

	@Override
	public User get(String userId) {
		return this.getMapper(UserMapper.class).selectByUserId(userId);
	}

}
