package kr.co.itcen.mysite.action.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.itcen.mysite.dao.BoardDao;
import kr.co.itcen.mysite.vo.BoardVo;
import kr.co.itcen.mysite.vo.PageVo;
import kr.co.itcen.web.WebUtils;
import kr.co.itcen.web.mvc.Action;

public class SearchlistAction implements Action{

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String kwd = request.getParameter("kwd");
		String page = request.getParameter("page");
		int pageConvertToInt = 0;
		
		pageConvertToInt = Integer.parseInt(page);
		PageVo pageInfo = new PageVo();

		int rowsPerPage = 5; // 페이지에 출력되는 데이터 갯수
		int pagesPerBlock = 5; // 페이지에 출력되는 블럭(숫자값) 갯수
		int currentPage = pageConvertToInt; // 현재 페이지 값
		int currentBlock = 0; // 현재 블럭 값
		
		if(currentPage % pagesPerBlock == 0) {
			currentBlock = currentPage / pagesPerBlock; // 현재 블럭 값
		} else {
			currentBlock = (currentPage / pagesPerBlock) + 1; // 현재 블럭 값
		}
		
		// 한 페이지에 ◀ 1 2 3 4 5 ▶ 다섯개의 블럭 출력
		// ◀ startRow 2 3 4 endRow ▶
		int startRow = (currentPage - 1) * rowsPerPage + 1; // 시작 줄
		int endRow = currentPage * rowsPerPage; // 끝나는 줄
		
		int totalRows = new BoardDao().getSearchCount(kwd); // 
		System.out.println("totalRows : " + totalRows);
		
		int totalPages = 0;

		if (totalRows % rowsPerPage == 0) {
			totalPages = totalRows / rowsPerPage; 
		} else {
			totalPages = totalRows / rowsPerPage + 1;
		}

		int totalBlocks = 0;
		if (totalPages % pagesPerBlock == 0) {
			totalBlocks = totalPages / pagesPerBlock;
		} else {
			totalBlocks = totalPages / pagesPerBlock + 1;
		}
		
		pageInfo.setCurrentPage(currentPage);
		pageInfo.setCurrentBlock(currentBlock);
		pageInfo.setRowsPerPage(rowsPerPage);
		pageInfo.setPagesPerBlock(pagesPerBlock);
		pageInfo.setStartRow(startRow);
		pageInfo.setEndRow(endRow);
		pageInfo.setTotalBlocks(totalBlocks);
		pageInfo.setTotalPages(totalPages);
		pageInfo.setTotalRows(totalRows);
		
		List<BoardVo> list = new BoardDao().search(kwd, startRow - 1, rowsPerPage);
		
		request.setAttribute("list", list);
		request.setAttribute("pageInfo", pageInfo);
		request.setAttribute("kwd", kwd);
		
		WebUtils.forward(request, response, "/WEB-INF/views/board/list.jsp");
	}

}
