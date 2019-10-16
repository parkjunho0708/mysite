package kr.co.itcen.mysite.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.itcen.mysite.security.Auth;
import kr.co.itcen.mysite.security.AuthUser;
import kr.co.itcen.mysite.security.Auth.Role;
import kr.co.itcen.mysite.service.UserService;
import kr.co.itcen.mysite.vo.UserVo;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@RequestMapping(value="/joinsuccess", method=RequestMethod.GET)
	public String joinsuccess() {
		return "user/joinsuccess";
	}
	
	@RequestMapping(value="/join", method=RequestMethod.GET)
	public String join(@ModelAttribute UserVo vo) {
		return "user/join";
	}

	@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(
		@ModelAttribute @Valid UserVo vo,
		BindingResult result,
		Model model) {
		
		if( result.hasErrors() ) {
			model.addAllAttributes(result.getModel());
			return "user/join";
		}
		
		userService.join(vo);
		return "redirect:/user/joinsuccess";
	}

	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login() {
		return "user/login";
	}
	
	// 로그인된 사용자의 authUser.no를 통해서 updata.jsp에 접근 및 해당 유저 정보 호출
//	@RequestMapping(value = "/update", method = RequestMethod.GET)
//	public String update(
//			@ModelAttribute UserVo vo, 
//			@RequestParam("no") Long no, 
//			Model model) {
//		vo = userService.getUpdate(no);
//		model.addAttribute("vo", vo);
//		return "user/update";
//	}

	// 회원정보수정하고 업데이트된 정보 다시 update.jsp로 호출
//	@RequestMapping(value = "/update", method = RequestMethod.POST)
//	public String update(
//			@ModelAttribute UserVo vo,
//			HttpServletResponse response,
//			Model model) {
//		userService.update(vo);
//		vo = userService.selectUpdatedUserData(vo.getNo());
//		model.addAttribute("vo", vo);
//		
//		response.setContentType("text/html; charset=UTF-8");
//		PrintWriter out;
//		try {
//			out = response.getWriter();
//			out.println("<script>alert('회원정보가 수정되었습니다.');</script>");
//			out.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return "user/update";
//	}
	
	@Auth(role = Role.USER)
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(@AuthUser UserVo authUser, Model model) {
		System.out.println("update : " + authUser);
		authUser = userService.getUser(authUser.getNo());
		model.addAttribute("userVo", authUser);

		if (authUser == null) {
			return "redirect:/main";
		}

		return "user/update";
	}

	@Auth(role = Role.USER)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(
			@ModelAttribute @Valid UserVo vo, 
			BindingResult result) {
		return "user/update";
	}

}