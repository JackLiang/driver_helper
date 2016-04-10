package com.dh.common.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RateUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(RateUtils.class);
	
	/**
	 * 根据知道概率返回数组的下标
	 * 
	 * @param rates
	 *            概率数组
	 * @return -1表示异常，非-1正常
	 */
	public static int getIndexByRate(Integer[] rates) {

		if (ArrayUtils.isEmpty(rates)) {
			LOG.error("概率数组没有数据，请检查。");
			return -1;
		}

		int range = 0;
		for (int rate : rates) {// 概率总分布范围
			range += rate;
		}

		int random = RandomUtils.nextInt(1, range);
		int sum = 0;
		for (int i = 0; i < rates.length; i++) {
			if (random > sum && random <= sum + rates[i]) {
				return i;
			}
			sum += rates[i];
		}

		return -1;

	}

}
