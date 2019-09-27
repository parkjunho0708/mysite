package kr.co.itcen.mysite.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.itcen.mysite.service.UserService;
import kr.co.itcen.mysite.vo.UserVo;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final Log LOG = LogFactory.getLog(UserController.class);

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/joinsuccess", method = RequestMethod.GET)
	public String joinsuccess() {
		return "user/joinsuccess";
	}

	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public String join() {
		return "user/join";
	}

	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String join(@ModelAttribute UserVo vo) {
		System.out.println("userService.join(vo)");
		
		if(vo.getEmail().indexOf("@") < 0) {
			// Validation Error
			// model.addAttribute("userVo", vo);
			return "user/join";
		}
		
		userService.join(vo);
		return "redirect:/user/joinsuccess";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "user/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(UserVo vo, HttpSession session, Model model) {
		UserVo userVo = userService.getUser(vo);
		if (userVo == null) {
			model.addAttribute("result", "fail");
			return "user/login";
		}

		// 로그인 처리
		session.setAttribute("authUser", userVo);
		return "redirect:/";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		// 접근제어 (ACL)
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser != null) {
			session.removeAttribute("authUser");
			session.invalidate();
		}

		return "redirect:/";
	}

	// 로그인된 사용자의 authUser.no를 통해서 updata.jsp에 접근 및 해당 유저 정보 호출
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(@ModelAttribute UserVo vo, @RequestParam("no") Long no, Model model) {
		vo = userService.getUpdate(no);
		model.addAttribute("vo", vo);
		return "user/update";
	}

	// 회원정보수정하고 업데이트된 정보 다시 update.jsp로 호출
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(@ModelAttribute UserVo vo, Model model) {
		userService.update(vo);
		vo = userService.selectUpdatedUserData(vo.getNo());
		model.addAttribute("vo", vo);
		return "user/update";
	}

//	@ExceptionHandler(UserDaoException.class)
//	public String handlerException() {
//		return "error/exception";
//	}

}
