package kr.co.itcen.mysite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itcen.mysite.repository.UserDao;
import kr.co.itcen.mysite.vo.UserVo;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	public void join(UserVo vo) {
		System.out.println("userDao.insert(vo)");
		userDao.insert(vo);
	}

	public UserVo getUser(UserVo vo) {
		return userDao.get(vo);
	}

	// 회원정보수정 페이지에 접속했을 때, 해당 유저의 정보를 update.jsp에 호출
	public UserVo getUpdate(Long no) {
		return userDao.getUpdate(no);
	}

	// 수정된 정보를 업데이트 해줌.
	public boolean update(UserVo vo) {
		return userDao.update(vo);
	}

	// 수정된 사용자 정보를 update.jsp로 다시 불러옴
	public UserVo selectUpdatedUserData(Long no) {
		return userDao.selectUpdatedUserData(no);
	}

	public Boolean existUser(String email) {
		return userDao.get(email) != null;
	}
}
