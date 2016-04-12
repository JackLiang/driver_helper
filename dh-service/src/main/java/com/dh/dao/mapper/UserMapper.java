package com.dh.dao.mapper;

import java.util.Map;

import com.dh.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    User selectByAccountAndPassword(Map<String,Object> map);
    
    User selectByUserId(String userId);
}