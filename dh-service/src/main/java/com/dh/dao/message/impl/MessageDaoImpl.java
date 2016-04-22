package com.dh.dao.message.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dh.common.BaseDao;
import com.dh.dao.mapper.MessageMapper;
import com.dh.dao.message.MessageDao;
import com.dh.model.Message;
import com.dh.vo.message.MessageCriteria;

@Repository("messageDao")
public class MessageDaoImpl extends BaseDao implements MessageDao {

	@Override
	public int insert(Message message) {
		return this.getMapper(MessageMapper.class).insertSelective(message);
	}

	@Override
	public List<Message> listByAddress(MessageCriteria mc) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("address", mc.getAddress());
		map.put("interest", mc.getInterest());
		map.put("limit", mc.getLimit());
		
		return this.getMapper(MessageMapper.class).selectByAddress(map);
	}

	@Override
	public Message get(int id) {
		return this.getMapper(MessageMapper.class).selectByPrimaryKey(id);
	}

	@Override
	public int update(Message message) {
		return this.getMapper(MessageMapper.class).updateByPrimaryKeySelective(message);
	}

}
