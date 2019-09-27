package kr.co.itcen.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import kr.co.itcen.mysite.vo.BoardVo;
import kr.co.itcen.mysite.vo.UserVo;

public class BoardDao {

	// 게시글 삽입
	public Boolean insert(BoardVo vo) {
		Boolean result = false;

		Connection connection = null;
		Statement stmt = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			connection = getConnection();

			String sql = "insert into board values (null, ?, ?, ?, now(), (select ifnull(max(g_no) + 1, 1) from board as getGroupNo), ?, ?, ?, ?, ?)"; // jdbc는 ;(세미콜론)이 있으면 쿼리가 또 있다고 인식
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setInt(3, vo.getHit());
			pstmt.setInt(4, vo.getOrderNo());
			pstmt.setInt(5, vo.getDepth());
			pstmt.setString(6, vo.getFile());
			pstmt.setString(7, "true");
			pstmt.setLong(8, vo.getUserNo());

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
	
	// group no, order no, depth 값 가지고 오기 (답글 등록으로 이동하기전)
	public BoardVo getGroupOrderDepth(Long boardNo) {
		BoardVo boardVo = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = getConnection();

			String sql = "select g_no, o_no, depth from board where no = ?";

			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, boardNo);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				int groupNo = rs.getInt(1);
				int orderNo = rs.getInt(2);
				int depth = rs.getInt(3);
				
				boardVo = new BoardVo();
				boardVo.setGroupNo(groupNo);
				boardVo.setOrderNo(orderNo);
				boardVo.setDepth(depth);
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
	
	// 답글 삽입 - 1
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
			
			String sql2 = "insert into board values (null, ?, ?, ?, now(), ?, ?, ?, ?, ?, ?)";
			pstmt2 = connection.prepareStatement(sql2);
			pstmt2.setString(1, vo.getTitle());
			pstmt2.setString(2, vo.getContents());
			pstmt2.setInt(3, vo.getHit());
			pstmt2.setInt(4, vo.getGroupNo());
			pstmt2.setInt(5, vo.getOrderNo());
			pstmt2.setInt(6, vo.getDepth());
			pstmt2.setString(7, vo.getFile());
			pstmt2.setString(8, "true");
			pstmt2.setLong(9, vo.getUserNo());

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
	
	// 답글 삽입 - 2
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

	// 처음에 리스트에 출력할 때 실행하는 메소드
	public List<BoardVo> getList(int startNum, int lastNum) {
		List<BoardVo> result = new ArrayList<BoardVo>();

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = getConnection();
			
			String sql = "select "
					+ "board.no, board.title, user.name, board.hit, date_format(board.reg_date, '%Y-%m-%d %h:%i:%s'), board.g_no, board.o_no, board.depth, user.no "
					+ "from user, board "
					+ "where user.no = board.user_no and board.status = 'true' "
					+ "order by board.g_no desc, board.o_no asc "
					+ "limit ?, ?";

			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, startNum);
			pstmt.setInt(2, lastNum);
			
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
				Long userNo = rs.getLong(9);

				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setUserName(userName);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setGroupNo(groupNo);
				vo.setOrderNo(orderNo);
				vo.setDepth(depth);
				vo.setUserNo(userNo);

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
	
	// 댓글 상세보기로 이동
	// 댓글 수정 여부를 판별하기 위한 user no 가져오기
	// 수정하기 위한 데이터 불러오기
	public BoardVo viewGetOne(Long boardNo, String username) {
		BoardVo boardVo = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = getConnection();

			String sql = "select "
					+ "board.no, "
					+ "board.title, "
					+ "board.contents, "
					+ "board.file, "
					+ "user.no, "
					+ "user.name "
					+ "from board, user "
					+ "where board.no = ? and user.name = ?";

			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, boardNo);
			pstmt.setString(2, username);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				String contents = rs.getString(3);
				String file = rs.getString(4);
				Long userNum = rs.getLong(5);
				String userName = rs.getString(6);
				
				boardVo = new BoardVo();
				boardVo.setNo(no);
				boardVo.setTitle(title);
				boardVo.setContents(contents);
				boardVo.setFile(file);
				boardVo.setUserNo(userNum);
				boardVo.setUserName(userName);
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
	public boolean modify(BoardVo vo) {
		Boolean result = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection();

			String sql = "update board set title = ?, contents = ? where no = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setLong(3, vo.getNo());
			
			int count = pstmt.executeUpdate();
			result = (count == 1);
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
		return result;
	}
	
	// 조회수 처리
	public Boolean hitUpdate(Long boardNo) {
		Boolean result = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection();

			String sql = "update board set hit = hit + 1 where no = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, boardNo);
			
			int count = pstmt.executeUpdate();
			result = (count == 1);
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
		return result;
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
			String sql = "update board set status = false where no = ? and (select user.password from user where user.no = ? and user.password = ?)";
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
	
	// count all data for pagination
	public int getCount() {
		int boardAllCount = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = getConnection();

			String sql = "select count(*) from board";

			pstmt = connection.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				boardAllCount = rs.getInt(1);
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
		return boardAllCount;
	}
	
	// count searched data for pagination
	public int getSearchCount(String kwd) {
		int boardSearchCount = 0;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = getConnection();

			String sql = "select count(*) "
					+ "from user, board "
					+ "where user.no = board.user_no "
					+ "and (board.title like ? or board.contents like ?) "
					+ "order by board.g_no desc, board.o_no asc";

			pstmt = connection.prepareStatement(sql);
			System.out.println("kwd : " + kwd);
			pstmt.setString(1, "%" + ((kwd == null) ? "" : kwd) + "%");
			pstmt.setString(2, "%" + ((kwd == null) ? "" : kwd) + "%");
			
			rs = pstmt.executeQuery();

			if (rs.next()) {
				boardSearchCount = rs.getInt(1);
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
		return boardSearchCount;
	}

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
