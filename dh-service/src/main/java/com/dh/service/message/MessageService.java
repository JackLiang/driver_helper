package com.dh.service.message;

import java.util.List;

import com.dh.model.Message;
import com.dh.vo.message.MessageCriteria;

public interface MessageService {
	
	/**上报消息
	 * @param message
	 * @return
	 */
	public boolean send(Message message);
	
	/**h获取消息列表
	 * @param mc
	 * @return
	 */
	public List<Message> getByCriteria(MessageCriteria mc);
	
	
	/**评价（不作并发考虑）
	 * @param msgId
	 * @param type1有用2无用
	 */
	public void evaluate(int msgId,int type);

}
