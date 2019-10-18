package kr.co.itcen.mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.itcen.mysite.vo.BoardVo;
import kr.co.itcen.mysite.vo.UserVo;

@Repository
public class BoardDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	// 현재 게시판에 저장되어 있는 모든 데이터의 갯수 출력
	public int getCount() {
		int allBoardCount = sqlSession.selectOne("board.allBoardCount");
		System.out.println("allBoardCount : " + allBoardCount);
		return allBoardCount;
	}

	// 처음에 리스트에 출력할 때 실행하는 메소드
	public List<BoardVo> getList(int startNum, int lastNum) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("startNum", startNum);
		map.put("lastNum", lastNum);
		return sqlSession.selectList("board.allBoardDataList", map);
	}

	// 게시글 삽입
	public Boolean insert(BoardVo vo) {
		int count = sqlSession.insert("board.insert", vo);
		return count == 1;
	}
	
	// 검색된 게시글 리스트 출력
	public List<BoardVo> search(int startNum, int lastNum, String kwd) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startNum", startNum);
		map.put("lastNum", lastNum);
		map.put("kwd", kwd);
		return sqlSession.selectList("board.searchList", map);
	}
	
	// 검색된 데이터의 갯수를 카운트
	public int getSearchCount(String kwd) {
		int boardSearchCount = sqlSession.selectOne("board.countAllPostData", kwd);
		return boardSearchCount;
	}

	// 조회수 처리
	public void hitUpdate(Long boardNo) {
		sqlSession.update("board.increaseHit", boardNo);
	}
	
	// 댓글 상세보기로 이동
	// 댓글 수정 여부를 판별하기 위한 user no 가져오기
	// 수정하기 위한 데이터 불러오기
	public BoardVo viewGetOne(Long boardNo, String userName) {
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println("boardNo : " + boardNo);
		map.put("boardNo", boardNo);
		map.put("userName", userName);
		return sqlSession.selectOne("board.getOnePostData", map);
	}
	
	// 수정한 게시글 데이터 업데이트
	public void modify(BoardVo boardVo) {
		sqlSession.update("board.updateModifiedData", boardVo);
	}

	// 게시글 수정후, 데이터 다시 불러오기 (수정데이터 확인)
	public BoardVo viewGetOne(BoardVo boardVo) {
		return sqlSession.selectOne("board.getOneModifiedPostData", boardVo);
	}
	
	// group no, order no, depth 값 가지고 오기 (답글 등록으로 이동하기전)
	public BoardVo getGroupOrderDepth(Long boardNo) {
		return sqlSession.selectOne("board.getGroupOrderDepthNo", boardNo);
	}
	
	public void replyUpdateOrderGroupNo(int groupNo, int orderNo) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		System.out.println("groupNo : " + groupNo);
		map.put("groupNo", groupNo);
		map.put("orderNo", orderNo);
		sqlSession.update("board.updateOrderGroupNo", map);
	}
	
	public void replyInsert(BoardVo vo) {
		sqlSession.insert("board.insertReplyData", vo);
	}
	
	// 게시글 삭제
	public void delete(Long boardNo, Long userNo, String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("boardNo", boardNo);
		map.put("userNo", userNo);
		map.put("password", password);
		sqlSession.delete("board.deleteData", map);
	}

}
