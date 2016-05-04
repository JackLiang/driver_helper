package com.dh.service.message.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dh.common.map.MapUtils;
import com.dh.dao.message.MessageDao;
import com.dh.model.Message;
import com.dh.service.message.MessageService;
import com.dh.vo.message.MessageCriteria;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

	@Resource(name = "messageDao")
	private MessageDao messageDao;

	/**
	 * 推送范围限制2公里
	 */
	private static final double EFFECTIVE_RANGE = 2000d;

	@Override
	public boolean send(Message message) {
		return messageDao.insert(message) > 0 ? true : false;
	}

	@Override
	public List<Message> getByCriteria(MessageCriteria mc) {

		String[] ltp = StringUtils.split(mc.getLocation(), ",");

		List<Message> msgs = messageDao.listByAddress(mc);
		List<Message> needSendMsgs = new ArrayList<Message>();
		if (msgs != null && !msgs.isEmpty()) {
			for (Message m : msgs) {
				String[] ltd = StringUtils.split(m.getLocation(), "-");
				// 用户当前请求位置范围2公里内的数据才推送
				if (MapUtils.getShortestDistance(Double.parseDouble(ltp[0]), Double.parseDouble(ltp[1]),
						Double.parseDouble(ltd[0]), Double.parseDouble(ltd[1])) < EFFECTIVE_RANGE) {
					needSendMsgs.add(m);

				}

			}
		}

		return needSendMsgs;
	}

	/*
	 * 评价（不作并发考虑）
	 * 
	 * @see com.dh.service.message.MessageService#evaluate(int, int)
	 */
	@Transactional
	public void evaluate(int msgId, int type) {

		Message msg = messageDao.get(msgId);
		if (msg == null) {
			return;
		}

		if (type == 1) {
			msg.setIsUsed(msg.getIsUsed() + 1);
		} else if (type == 2) {
			msg.setNoUsed(msg.getNoUsed() + 1);
		}

		messageDao.update(msg);

	}

}
