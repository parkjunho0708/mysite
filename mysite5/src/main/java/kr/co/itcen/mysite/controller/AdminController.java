package kr.co.itcen.mysite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.itcen.mysite.security.Auth;
import kr.co.itcen.mysite.security.Auth.Role;

@Auth(role=Role.ADMIN)
@Controller
@RequestMapping("/admin")
public class AdminController {

	@RequestMapping("")
	public String main() {
		return "admin/main";
	}
	
	@RequestMapping("/guestbook")
	public String guestbook() {
		return "admin/guestbook";
	}
	
	@RequestMapping("/board")
	public String board() {
		return "admin/board";
	}
	
	@RequestMapping("/user")
	public String user() {
		return "admin/user";
	}
}