package com.dh.dao.message;


import java.util.List;

import com.dh.model.Message;
import com.dh.vo.message.MessageCriteria;

public interface MessageDao {
	
	public int insert(Message message);
	
	public List<Message> listByAddress(MessageCriteria mc);

}
