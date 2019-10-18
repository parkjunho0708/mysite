package kr.co.itcen.mysite.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import kr.co.itcen.mysite.vo.UserVo;

public class AuthUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	// 바인딩할 객체를 조작할 수 있는 메서드
	@Override
	public Object resolveArgument(
			MethodParameter parameter,
			ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {

		if(supportsParameter(parameter) == false) {
			return WebArgumentResolver.UNRESOLVED;
		}
		
		HttpServletRequest request = 
				webRequest.getNativeRequest(HttpServletRequest.class);
		
		HttpSession session = request.getSession();
		if(session == null) {
			return null;
		}
		
		return session.getAttribute("authUser");
	}

	// 바인딩할 클래스를 지정해주는 메서드
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// @AuthUser가 붙어 있는지 확인
		AuthUser authUser = parameter.getParameterAnnotation(AuthUser.class);
		
		// @AuthUser가 없으면,
		if(authUser == null) {
			return false;
		}
		
		// 파라미터 타입이 UserVo 인지 확인
		// @AuthUser가 붙어있는데 UserVo가 아니라 
		// BoardVo라던지 파라미터 타입이 다를때, 클래스의 객체 비교
		if(parameter.getParameterType().equals(UserVo.class) == false) {
			return false;
		}
		
		return true;
	}

}