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
	
	
	
	
	
	// 답글 삽입
	public Boolean replyUpdate(BoardVo vo) {
		Boolean result = false;
		Connection connection = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		try {
			connection = getConnection();

			String sql1 = "update "
										+ "board "
										+ "set o_no = o_no + 1 "
										+ "where g_no = ? and o_no >= ?";
			pstmt1 = connection.prepareStatement(sql1);
			pstmt1.setInt(1, vo.getGroupNo());
			pstmt1.setInt(2, vo.getOrderNo());
			
			pstmt1.executeUpdate();
			
			String sql2 = "insert into board values (null, ?, ?, ?, now(), ?, ?, ?, ?, ?)";
			pstmt2 = connection.prepareStatement(sql2);
			pstmt2.setString(1, vo.getTitle());
			pstmt2.setString(2, vo.getContents());
			pstmt2.setInt(3, vo.getHit());
			pstmt2.setInt(4, vo.getGroupNo());
			pstmt2.setInt(5, vo.getOrderNo());
			pstmt2.setInt(6, vo.getDepth());
			//pstmt2.setString(7, vo.getImage());
			pstmt2.setLong(8, vo.getUserNo());

			int count = pstmt2.executeUpdate();
			result = (count == 1);
		} catch (SQLException e) {
			System.out.println("error : " + e);
		} finally {
			try {
				if (pstmt1 != null) {
					pstmt1.close();
				}
				if (pstmt2 != null) {
					pstmt2.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	// 답글 삽입
	public Boolean replyInsert(BoardVo vo) {
		Boolean result = false;

		Connection connection = null;
		Statement stmt = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();

			String sql = "insert into board values (null, ?, ?, ?, now(), ?, ?, ?, ?)"; // jdbc는 ;(세미콜론)이 있으면 쿼리가 또 있다고 인식
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setInt(3, vo.getHit());
			pstmt.setInt(4, vo.getGroupNo());
			pstmt.setInt(5, vo.getOrderNo());
			pstmt.setInt(6, vo.getDepth());
			pstmt.setLong(7, vo.getUserNo());

			int count = pstmt.executeUpdate();
			result = (count == 1);

			stmt = connection.createStatement();
			rs = stmt.executeQuery("select last_insert_id()");
			if (rs.next()) {
				Long no = rs.getLong(1);
				vo.setNo(no);
			}
		} catch (SQLException e) {
			System.out.println("error : " + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	
	public int getAllDataCount() {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int getAllCount = 0;

		try {
			connection = getConnection();

			String sql = "select count(*) from board";
			
			pstmt = connection.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				getAllCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("error : " + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return getAllCount;
	}
	
	
	
	public BoardVo viewGetDetail(Long boardNo) {
		BoardVo boardVo = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = getConnection();

			String sql = "select board.no, board.title, board.contents from board, user where board.no = ? group by board.no";

			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, boardNo);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				String contents = rs.getString(3);
				
				boardVo = new BoardVo();
				boardVo.setNo(no);
				boardVo.setTitle(title);
				boardVo.setContents(contents);
			}
		} catch (SQLException e) {
			System.out.println("error : " + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return boardVo;
	}
	
	// 댓글 수정하기
	
	
	// set max(no) 게시글의 번호를 그룸번호에 등록하기 위한 메소드
	public int getMaxNoFromBoard() {
		int boardNo = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;

		try {
			connection = getConnection();

			String sql = "select max(no) from board;";

			pstmt = connection.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				boardNo = rs.getInt(1);
			}
			
			String sql2 = "update board set max(n0 = where max(no) = ?";
			pstmt2 = connection.prepareStatement(sql2);
			pstmt2.setInt(1, boardNo);
			
		} catch (SQLException e) {
			System.out.println("error : " + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return boardNo;
	}
	
	
	
	// 검색결과
	public List<BoardVo> search(String kwd, int startNum, int lastNum) {
		List<BoardVo> result = new ArrayList<BoardVo>();

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = getConnection();

			String sql = "select "
					+ "board.no, "
					+ "board.title, "
					+ "user.name, "
					+ "board.hit, "
					+ "date_format(board.reg_date, '%Y-%m-%d %h:%i:%s'), "
					+ "board.g_no, "
					+ "board.o_no, "
					+ "board.depth "
					+ "from user, board "
					+ "where user.no = board.user_no and (board.title like ? or board.contents like ?) "
					+ "order by board.g_no desc, board.o_no asc limit ?, ?";

			pstmt = connection.prepareStatement(sql);
			System.out.println("kwd : " + kwd);
			pstmt.setString(1, "%" + ((kwd == null) ? "" : kwd) + "%");
			pstmt.setString(2, "%" + ((kwd == null) ? "" : kwd) + "%");
			pstmt.setInt(3, startNum);
			pstmt.setInt(4, lastNum);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				String userName = rs.getString(3);
				int hit = rs.getInt(4);
				String regDate = rs.getString(5);
				int groupNo = rs.getInt(6);
				int orderNo = rs.getInt(7);
				int depth = rs.getInt(8);

				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setUserName(userName);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setGroupNo(groupNo);
				vo.setOrderNo(orderNo);
				vo.setDepth(depth);

				result.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error : " + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public void delete(Long boardNo, Long userNo, String password) {
		Connection connection = null;
		PreparedStatement pstmt = null;

		try {
			connection = getConnection();
			String sql = "update board set where no = ? and (select user.password from user where user.no = ? and ?);";
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, boardNo);
			pstmt.setLong(2, userNo);
			pstmt.setString(3, password);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error : " + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// count searched data for pagination
	
	private Connection getConnection() throws SQLException {
		Connection connection = null;

		try {
			Class.forName("org.mariadb.jdbc.Driver");
			String url = "jdbc:mariadb://192.168.1.93:3306/webdb?characterEncoding=utf8";
			connection = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("Fail to Loading Driver : " + e);
		}

		return connection;
	}

}
