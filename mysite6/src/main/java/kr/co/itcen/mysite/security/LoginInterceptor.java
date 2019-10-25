package kr.co.itcen.mysite.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.itcen.mysite.service.UserService;
import kr.co.itcen.mysite.vo.UserVo;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		UserVo vo = new UserVo();
		vo.setEmail(email);
		vo.setPassword(password);

		System.out.println("userService : " + userService);
		UserVo authUser = userService.getUser(vo);
		System.out.println("authUser : " + authUser);
		if (authUser == null) {
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}

		// session 처리
		HttpSession session = request.getSession(true);
		session.setAttribute("authUser", authUser);
		System.out.println("LoginInterceptor : " + authUser);

		response.sendRedirect(request.getContextPath() + "/");
		return false;
	}
}