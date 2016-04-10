package com.dh.common.utils;
/**
 * 
 * @author jiayu_zheng
 *
 */
public class StrUtil {

	/**
	 * 去掉 “ 去掉{，去掉}
	 * @param content
	 * @return
	 */
	public static String replaceJsonprefix(String content){
		//不想写正则了.................
		if(content!=null){
			content = content.replace("{","");
			content = content.replace("}","");
			content = content.replace("\"","");
		}
		return content;
	}
	
	public static void main(String args[]){
		String json = "{\"颜色\":\"绿色\",\"尺寸\":\"XXXL\"}";
		
		System.out.println(replaceJsonprefix(json));
	}
}
