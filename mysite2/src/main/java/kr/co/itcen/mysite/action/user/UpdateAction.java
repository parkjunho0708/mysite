package kr.co.itcen.mysite.action.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.itcen.mysite.dao.UserDao;
import kr.co.itcen.mysite.vo.UserVo;
import kr.co.itcen.web.WebUtils;
import kr.co.itcen.web.mvc.Action;

public class UpdateAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserVo userVo = null;
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		userVo = new UserVo();
		userVo.setNo(authUser.getNo());
		userVo.setName(name);
		userVo.setEmail(email);
		userVo.setGender(gender);
		new UserDao().update(userVo);
		
		userVo = new UserDao().selectUpdatedUserData(authUser.getNo());
		request.setAttribute("updateUser", userVo);
		session.setAttribute("authUser", userVo);
		WebUtils.forward(request, response, "/WEB-INF/views/user/updateform.jsp");
	}

}
