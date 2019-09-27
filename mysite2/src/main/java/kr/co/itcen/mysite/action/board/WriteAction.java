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

public class WriteAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		String contents = request.getParameter("contents");

		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");

		BoardVo boardVo = new BoardVo();
		boardVo.setTitle(title);
		boardVo.setContents(contents);
		boardVo.setHit(0);
		boardVo.setOrderNo(1);
		boardVo.setDepth(0);
		boardVo.setUserNo(authUser.getNo());

		Part part = request.getPart("file"); // file 태그에서 지정해준 name을 통해서 업로드된 파일의 정보를 가지고 옴.
		String fileName = getFilename(part); // 데이터베이스에 저장하기 위한 파일 이름 출력
		
		if (fileName != null && !fileName.isEmpty()) {
			part.write(fileName);
		}

		boardVo.setFile(fileName);
		new BoardDao().insert(boardVo);

		WebUtils.redirect(request, response, request.getContextPath() + "/board?a=list");
	}

	private String getFilename(Part part){
        String fileName = null;        
        String contentDispositionHeader = part.getHeader("content-disposition");
        System.out.println("part.getHeader :"+part.getHeader("content-disposition"));
        String [] elements = contentDispositionHeader.split(";");
        System.out.println(elements);
        
        for(String element: elements){
            fileName = element.substring(element.indexOf('=')+1);
            fileName = fileName.trim().replace("\"","");
        }        
        return fileName;        
    }
}
