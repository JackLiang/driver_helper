/**
 *  
 */
package com.dh.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dh.common.utils.http.HttpUtils;

/**
 * @author haibin_chen 2016年2月20日
 *
 */
public class SmsUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(SmsUtils.class);
	
	private static String URL="http://yunpian.com/v1/sms/tpl_send.json";
	private static String APIKEY="56d53a93e040eff9822f5edd4c64b4b1";
	
	/**
	 * 发送短信验证码
	 * 
	 * @param mobile
	 * @param content
	 * @return 成功返回true，否则false
	 */
	public static boolean sendVerifyCode(String mobile, String verifyCode){
		try {
			if (StringUtils.isBlank(mobile) || StringUtils.isBlank(verifyCode)) {
				return false;
			}
			
			String code="#code#=" + verifyCode;
			String tplId="1254943";
			
			
			Map<String,String> map=new HashMap<String,String>();
			map.put("apikey", APIKEY);
			map.put("tpl_id", tplId);
			map.put("tpl_value", code);
			map.put("mobile", mobile);
		
			String result = HttpUtils.doHttpPost(URL, null, map, "utf-8");
			LOG.info("发送短信结果：{}", result);
			JSONObject resultObj = JSON.parseObject(result);
			if (resultObj.getIntValue("code") == 0) {
				return true;
			} else {
				LOG.error("发送短信异常:" + resultObj.getString("msg"));
				return false;
			}
			
		} catch (Exception e) {
			LOG.error("发送验证码短信异常", e);
			return false;
		}
	}
}
