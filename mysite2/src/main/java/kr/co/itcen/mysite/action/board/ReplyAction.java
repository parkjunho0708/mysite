package kr.co.itcen.mysite.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import kr.co.itcen.mysite.dao.BoardDao;
import kr.co.itcen.mysite.vo.BoardVo;
import kr.co.itcen.mysite.vo.UserVo;
import kr.co.itcen.web.WebUtils;
import kr.co.itcen.web.mvc.Action;

public class ReplyAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String groupNo = request.getParameter("groupno");
		String orderNo = request.getParameter("orderno");
		String depth = request.getParameter("depth");
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");

		String title = request.getParameter("title");
		String contents = request.getParameter("contents");

		BoardVo boardVo = new BoardVo();
		boardVo.setTitle(title);
		boardVo.setContents(contents);
		boardVo.setGroupNo(Integer.parseInt(groupNo));
		System.out.println("boardVo.setGroupNo(Integer.parseInt(groupNo)) : " + boardVo.getGroupNo());
		boardVo.setOrderNo(Integer.parseInt(orderNo) + 1);
		boardVo.setDepth(Integer.parseInt(depth) + 1);
		boardVo.setUserNo(authUser.getNo());
		
		Part part = request.getPart("file");
		String fileName = getFiles(part);
		if (fileName != null && !fileName.isEmpty()) {
			part.write(fileName);
		}

		boardVo.setFile(fileName);

		new BoardDao().replyUpdate(boardVo);

		WebUtils.redirect(request, response, request.getContextPath() + "/board?a=list");
	}
	
	private String getFiles(Part part) {
		String fileName = "";
		String contentDispositionHeader = part.getHeader("content-disposition");
		String[] elements = contentDispositionHeader.split(";");
		for (String e : elements) {
			fileName = e.substring(e.indexOf("=") + 1);
			fileName = fileName.trim().replace("\"", "");
		}
		return fileName;
	}

}
