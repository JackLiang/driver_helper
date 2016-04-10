package com.dh.vo;


import com.alibaba.fastjson.JSON;

/**
 * 全局返回实体类
 * @author jiayu_zheng
 *
 */
public class RespVO {

	/**0:成功；其他：失败*/
	private int code;
	
	/**提示信息*/
	private String msg;
	
	/** 如果有实体类，则返回 */
	private Object object;
	
	public RespVO(){
	}
	
	public RespVO(int code,String msg){
		this.code = code;
		this.msg = msg;
		this.object = "";
	}
	
	public RespVO(int code, String msg, Object object) {
		this.code = code;
		this.msg = msg;
		this.object = object;
	}
//	public RespVO(ExceptionType e){
//		this.code = e.getCode();
//		this.msg = e.getMsg();
//		this.object = "";
//	}
//	public RespVO(ExceptionType e, Object object){
//		this.code = e.getCode();
//		this.msg = e.getMsg();
//		this.object = object;
//	}
	
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public static void main(String[] args) {
		RespVO vo = new RespVO();
		vo.setCode(-1);
		vo.setMsg("error msg");
		System.out.println(JSON.toJSONString(vo));
	}
}
