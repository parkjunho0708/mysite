package kr.co.itcen.mysite.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import kr.co.itcen.mysite.service.BoardService;
import kr.co.itcen.mysite.service.FileUploadService;
import kr.co.itcen.mysite.vo.BoardVo;
import kr.co.itcen.mysite.vo.PageVo;
import kr.co.itcen.mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {

	private static final Log Log = LogFactory.getLog(BoardController.class);

	@Autowired
	private BoardService boardService;

	@Autowired
	FileUploadService fileUploadService;

	// navigation에서 게시판을 눌렀을 때, 실행되는 메소드
	@RequestMapping(value = "/list")
	public String index(
			@RequestParam int page, 
			Model model) {
		PageVo pageInfo = pagingProcess(page, null);
		List<BoardVo> list = boardService.getList(pageInfo.getStartRow() - 1, pageInfo.getRowsPerPage());
		model.addAttribute("list", list);
		model.addAttribute("pageInfo", pageInfo);
		return "board/list";
	}

	// write.jsp 페이지로 이동
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public String write() {
		return "board/write";
	}

	// 게시글 입력 (insert)
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String write(
			@ModelAttribute BoardVo boardVo, 
			HttpSession session, 
			HttpServletRequest request,
			MultipartFile mfile, 
			Model model) throws Exception {
		session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		boardVo.setUserNo(authUser.getNo());

		String getFileNameFromMf = fileUpload(mfile);
		boardVo.setFilename(getFileNameFromMf);
		boardService.insert(boardVo);
		return "redirect:/board/list?page=1";
	}

	// 검색한 데이터를 리스트로 출력해주는 메소드
	@RequestMapping(value = "/searchlist/{page}", method = RequestMethod.POST)
	public String searchlist(
			@PathVariable int page, 
			@RequestParam String kwd, 
			Model model) {
		PageVo pageInfo = pagingProcess(page, kwd);
		System.out.println("page : " + page);
		List<BoardVo> list = boardService.search(pageInfo.getStartRow() - 1, pageInfo.getRowsPerPage(), kwd);
		model.addAttribute("list", list);
		model.addAttribute("pageInfo", pageInfo);
		return "/board/list";
	}

	// 게시판에서 글을 선택했을 때, 게시글에 대한 정보 호출 및 조회수 증가
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(
			@RequestParam Long no, 
			@RequestParam String username, 
			HttpSession session,
			@ModelAttribute UserVo userVo, 
			@ModelAttribute BoardVo boardVo, 
			Model model) {
		boardService.hitUpdate(no);
		Long userno = 0L;

		if (session.getAttribute("authUser") != null) {
			userVo = (UserVo) session.getAttribute("authUser");
			userno = userVo.getNo();
			model.addAttribute("userno", userno);
		}

		boardVo = boardService.viewGetOne(no, username);
		model.addAttribute("vo", boardVo);
		return "/board/view";
	}

	// 수정폼으로 이동
	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public String modify(
			@RequestParam Long no, 
			@RequestParam String username, 
			@ModelAttribute BoardVo boardVo,
			HttpServletResponse response, 
			Model model) {
		boardVo = boardService.viewGetOne(no, username);
		model.addAttribute("vo", boardVo);
		return "/board/modify";
	}

	// 수정폼에서 데이터 수정
	@RequestMapping(value = "/modify/{no}", method = RequestMethod.POST)
	public String modify(
			@PathVariable Long no, 
			@ModelAttribute BoardVo boardVo, 
			HttpSession session,
			HttpServletRequest request, 
			HttpServletResponse response, 
			Model model) {
		// board no 값 설정
		boardService.modify(boardVo);

		session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		boardVo.setUserName(authUser.getName());
		boardVo.setNo(no);

		boardVo = boardService.viewGetOne(boardVo);
		model.addAttribute("vo", boardVo);

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println("<script>alert('회원정보가 수정되었습니다.');</script>");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "/board/modify";
	}

	// 답글폼으로 이동
	@RequestMapping(value = "/reply", method = RequestMethod.GET)
	public String reply(
			@ModelAttribute BoardVo boardVo, 
			@RequestParam Long no, 
			Model model) {
		boardVo = boardService.getGroupOrderDepth(no);
		model.addAttribute("vo", boardVo);
		return "/board/reply";
	}

	// 답글 작성
	@RequestMapping(value = "/reply/{no}/{groupNo}/{orderNo}/{depth}", method = RequestMethod.POST)
	public String reply(
			@PathVariable Long no, 
			@PathVariable int groupNo, 
			@PathVariable int orderNo,
			@PathVariable int depth, 
			@RequestParam("title") String title, 
			@RequestParam("contents") String contents,
			@ModelAttribute BoardVo boardVo, 
			HttpSession session, 
			MultipartFile mfile, 
			HttpServletRequest request,
			HttpServletResponse response, 
			Model model) {
		boardVo.setOrderNo(orderNo + 1);
		boardVo.setDepth(depth + 1);
		boardService.replyUpdateOrderGroupNo(groupNo, boardVo.getOrderNo());

		session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		boardVo.setUserNo(authUser.getNo());
		boardVo.setTitle(title);
		boardVo.setContents(contents);
		boardVo.setGroupNo(groupNo);
		boardVo.setStatus("true");

		String getFileNameFromMf = fileUpload(mfile);
		boardVo.setFilename(getFileNameFromMf);

		System.out.println("boardVo : " + boardVo);

		boardService.replyInsert(boardVo);
		System.out.println("board title 3 : " + title);
		System.out.println("order no  3 : " + orderNo);
		return "redirect:/board/list?page=1";
	}

	// 삭제 페이지로 이동
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(
			@RequestParam Long no, 
			Model model) {
		model.addAttribute("no", no);
		return "/board/delete";
	}

	// 삭제 페이지에서 비밀번호 입력해서 데이터 삭제
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(
			@RequestParam Long no, 
			@RequestParam String password, 
			HttpServletRequest request,
			Model model) {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		boardService.delete(no, authUser.getNo(), password);
		model.addAttribute("no", no);
		return "redirect:/board/list?page=1";
	}

	// 파일 다운로드
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void downloadFile(
			@RequestParam String filename, 
			HttpServletResponse response) {
		String sDownloadPath = "D:\\itcen\\eclipse-workspace\\mysite\\mysite3\\src\\main\\webapp\\assets\\images\\";
        String sFilePath = sDownloadPath + filename;
        
        byte b[] = new byte[4096];
        FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(sFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        String sMimeType = mimeTypesMap.getContentType(sFilePath);
        
		if (sMimeType == null) {
			sMimeType = "application/octet-stream";
		}
        
        response.setContentType(sMimeType);
        
        String sEncoding;
		try {
			sEncoding = new String(filename.getBytes("utf-8"));
			response.setHeader("Content-Disposition", "attachment; filename= " + sEncoding);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
        
        
        ServletOutputStream servletOutStream = null;
		try {
			servletOutStream = response.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        int numRead;
        try {
			while((numRead = fileInputStream.read(b,0,b.length))!= -1){
			    servletOutStream.write(b,0,numRead);            
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		try {
			servletOutStream.flush();
			servletOutStream.close();
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 파일 업로드
	public String fileUpload(MultipartFile fileUpload) {
		String imgPath = "assets\\images";
		String realPath = "D:\\itcen\\eclipse-workspace\\mysite\\mysite3\\src\\main\\webapp\\";
		System.out.println("realPath : " + realPath);
		String originalFileName;

		originalFileName = fileUpload.getOriginalFilename();

		StringBuffer path = new StringBuffer();
		path.append(realPath).append(imgPath).append("\\");
		path.append(originalFileName);
		System.out.println("path : " + path);

		if (!originalFileName.equals("")) {
			File f = new File(path.toString());
			try {
				fileUpload.transferTo(f);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
		return originalFileName;
	}

	// 페이징 처리 메소드
	public PageVo pagingProcess(int page, String kwd) {
		int pageConvertToInt = 0;

		if (page == 0) {
			pageConvertToInt = 1;
		} else {
			pageConvertToInt = page;
		}

		PageVo pageInfo = new PageVo();

		int rowsPerPage = 5;
		int pagesPerBlock = 5;
		int currentPage = pageConvertToInt;
		int currentBlock = 0;

		if (currentPage % pagesPerBlock == 0) {
			currentBlock = currentPage / pagesPerBlock;
		} else {
			currentBlock = (currentPage / pagesPerBlock) + 1;
		}

		// 한 페이지에 ◀ 1 2 3 4 5 ▶ 다섯개의 블럭 출력
		// ◀ startRow 2 3 4 endRow ▶
		int startRow = (currentPage - 1) * rowsPerPage + 1;
		int endRow = currentPage * rowsPerPage;

		// 현재 게시판에 있는 게시글의 총 갯수 출력
		int totalRows = 0;
		if (kwd != null) {
			totalRows = boardService.getSearchCount(kwd);
		} else {
			totalRows = boardService.getCount();
		}
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

//		System.out.println("======= page information =======");
//		System.out.println(pageInfo.getCurrentPage());
//		System.out.println(pageInfo.getCurrentBlock());
//		System.out.println(pageInfo.getRowsPerPage());
//		System.out.println(pageInfo.getPagesPerBlock());
//		System.out.println(pageInfo.getStartRow());
//		System.out.println(pageInfo.getEndRow());
//		System.out.println(pageInfo.getTotalBlocks());
//		System.out.println(pageInfo.getTotalPages());
//		System.out.println(pageInfo.getTotalRows());

		return pageInfo;
	}
}
