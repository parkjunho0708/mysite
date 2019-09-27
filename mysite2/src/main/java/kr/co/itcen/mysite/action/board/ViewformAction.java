package kr.co.itcen.mysite.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.itcen.mysite.dao.BoardDao;
import kr.co.itcen.mysite.dao.UserDao;
import kr.co.itcen.mysite.vo.BoardVo;
import kr.co.itcen.mysite.vo.UserVo;
import kr.co.itcen.web.WebUtils;
import kr.co.itcen.web.mvc.Action;

public class ViewformAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String no = request.getParameter("no");
		String username = request.getParameter("username");
		
		new BoardDao().hitUpdate(Long.parseLong(no));
		
		HttpSession session = request.getSession();
		UserVo authUser = null;
		Long userno = 0L;

		if (session.getAttribute("authUser") != null) {
			authUser = (UserVo) session.getAttribute("authUser");
			userno = authUser.getNo();
			request.setAttribute("userno", userno);
		}
		
		BoardVo vo = new BoardDao().viewGetOne(Long.parseLong(no), username);
		request.setAttribute("vo", vo);

		WebUtils.forward(request, response, "/WEB-INF/views/board/view.jsp");
	}

}