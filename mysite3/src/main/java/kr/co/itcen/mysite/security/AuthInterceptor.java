package kr.co.itcen.mysite.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.itcen.mysite.controller.AdminController;
import kr.co.itcen.mysite.vo.UserVo;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	// @RequestMapping 선언으로 요청에 대한 HandlerMethod(@Controller의 메서드)가 정해졌다면, 
	// handler라는 이름으로 HandlerMethod가 들어온다. 
	// HandlerMethod로 메서드 시그니처 등 추가적인 정보를 파악해서 로직 실행 여부를 판단할 수 있다.
	@Override
	public boolean preHandle(
		HttpServletRequest request,
		HttpServletResponse response,
		Object handler)
		throws Exception {
		
		//1. handler 종류(DefaultServletHttpRequestHandler, HandlerMethod) 
		if( handler instanceof HandlerMethod == false ) {
			return true;
		}
		
		//2. casting
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		
		//3. @Auth 받아오기
		// 자신이 선언해둔 어노테이션을 가져올 수 있다.
		Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
		System.out.println("auth : " + auth);
		
		// 4. @Auth가 없으면 class type에 있을 수 있으므로...
		if (auth == null) {
			auth = handlerMethod.getBeanType().getAnnotation(Auth.class);
			System.out.println("auth auth auth : " + auth);
			// 과제 : class type에서 @Auth가 있는지를 확인해봐야 한다.
			// 4. method에 @Auth가 없는 경우, 즉 인증이 필요 없는 요청
			if (auth == null) {
				return true;
			}
		}

		// 5. @Auth가 있는 경우이므로, 세션이 있는지 체크
		HttpSession session = request.getSession();
		if( session == null ) {
			// 로그인 화면으로 이동
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}
		
		// 6. 세션이 존재하면 유효한 유저인지 확인
		// @Auth가 class나 method에 붙어 있기 때문에 인증 여부를 체크한다.
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null || session.getAttribute("authUser") == null) {
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}
		
		//8. Method의 @Auth의 Role 가져오기
		String role = auth.role().toString();
		System.out.println("role : " + role);
		System.out.println("[[[[[auth]]]]] : " + auth);

		//9. 메소드의 @Auth의 Role이 "USER"인 경우.
		//   인증만 되어 있으면 모두 통과
		if("USER".equals(role)) {
			System.out.println("user user user");
			return true;
		}
		
		Auth adminRole = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Auth.class);
		//10. 메소드의 @Auth의 Role이 "ADMIN"인 경우
		// -- 과제
		// admin일 경우
		if (adminRole != null) {
			role = adminRole.role().toString();
			if ("ADMIN".equals(role)) {
				if ("ADMIN".equals(authUser.getRole()) == false) {
					response.sendRedirect(request.getContextPath());
					return false;
				}
			}
		}
		
		return true;
	}

}