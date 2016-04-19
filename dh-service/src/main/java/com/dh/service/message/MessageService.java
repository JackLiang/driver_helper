package com.dh.service.message;

import java.util.List;

import com.dh.model.Message;
import com.dh.vo.message.MessageCriteria;

public interface MessageService {
	
	public boolean send(Message message);
	
	public List<Message> getByCriteria(MessageCriteria mc);

}
