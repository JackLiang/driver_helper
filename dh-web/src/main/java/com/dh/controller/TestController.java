package com.dh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dh.vo.RespVO;

@Controller
@RequestMapping("/dh")
public class TestController {

	@ResponseBody
	@RequestMapping("/test.do")
	public Object test(){
		
		RespVO vo = new RespVO(0, "success");
		
		return JSON.toJSONString(vo);
	}
	
	
}
