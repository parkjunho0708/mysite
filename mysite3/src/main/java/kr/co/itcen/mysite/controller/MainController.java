package kr.co.itcen.mysite.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.itcen.mysite.vo.UserVo;

@Controller
public class MainController {

	private static final Log Log = LogFactory.getLog(MainController.class);

	@RequestMapping({ "", "/main" })
	public String index() {
		return "main/index";
	}

	@ResponseBody
	@RequestMapping("/hello")
	public String hello() {
		return "안녕하세요~";
	}
	
	@ResponseBody
	@RequestMapping("/hello2")
	public UserVo hello2() {
		UserVo vo = new UserVo();
		vo.setNo(10L);
		vo.setName("안대혁");
		vo.setEmail("kickscar@gmail.com");
		return vo;
	}
}
