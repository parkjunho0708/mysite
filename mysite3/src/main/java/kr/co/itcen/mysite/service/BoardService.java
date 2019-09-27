package kr.co.itcen.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itcen.mysite.repository.BoardDao;
import kr.co.itcen.mysite.vo.BoardVo;

@Service
public class BoardService {

	@Autowired
	private BoardDao boardDao;
	
	// 게시글 갯수 카운트
	public int getCount() {
		int allBoardCount = boardDao.getCount();
		System.out.println("allBoardCount : " + allBoardCount);
		return allBoardCount;
	}
	
	// 게시글 리스트 출력
	public List<BoardVo> getList(int startNum, int lastNum) {
		return boardDao.getList(startNum, lastNum);
	}
	
	// 게시글 추가
	public void insert(BoardVo vo) {
		boardDao.insert(vo);
	}

	// 검색된 게시글의 갯수 출력
	public int getSearchCount(String kwd) {
		int countAllPostData = boardDao.getSearchCount(kwd);
		return countAllPostData;
	}
	
	// 검색된 게시글 리스트 출력
	public List<BoardVo> search(int startNum, int lastNum, String kwd){
		return boardDao.search(startNum, lastNum, kwd);
	}
	
	// 리스트에 글 눌렀을 때, 조회수 증가
	public void hitUpdate(Long boardNo) {
		boardDao.hitUpdate(boardNo);
	}
	
	// 리스트에 글 눌렀을 때, 클릭한 해당 데이터의 정보 출력
	public BoardVo viewGetOne(Long boardNo, String userName) {
		return boardDao.viewGetOne(boardNo, userName);
	}
	
	// 사용자의 제목, 내용 수정
	public void modify(BoardVo boardVo) {
		boardDao.modify(boardVo);
	}
	
	// 댓글 수정후, 데이터 다시 불러오기 (수정데이터 확인)
	public BoardVo viewGetOne(BoardVo boardVo) {
		return boardDao.viewGetOne(boardVo);
	}
	
	// 댓글 수정후, 데이터 다시 불러오기 (수정데이터 확인)
	public BoardVo getGroupOrderDepth(Long boardNo) {
		return boardDao.getGroupOrderDepth(boardNo);
	}
	
	
	
}
