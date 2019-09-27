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

public class ListAction implements Action {
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String page = request.getParameter("page");
		int pageConvertToInt = 0;
		
		if (page == null) {
			pageConvertToInt = 1;
		} else {
			pageConvertToInt = Integer.parseInt(page);
		}
		
		PageVo pageInfo = new PageVo();

		int rowsPerPage = 5;
		int pagesPerBlock = 5;
		int currentPage = pageConvertToInt;
		int currentBlock = 0;
		
		if(currentPage % pagesPerBlock == 0) {
			currentBlock = currentPage / pagesPerBlock;
		} else {
			currentBlock = (currentPage / pagesPerBlock) + 1;
		}
		
		int startRow = (currentPage - 1) * rowsPerPage + 1;
		int endRow = currentPage * rowsPerPage;
		
		int totalRows = new BoardDao().getCount();
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
		
		List<BoardVo> list = new BoardDao().getList(startRow - 1, rowsPerPage);
		
		request.setAttribute("list", list);
		request.setAttribute("pageInfo", pageInfo);
		
		WebUtils.forward(request, response, "/WEB-INF/views/board/list.jsp");
	}

}
