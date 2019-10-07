package kr.co.itcen.mysite.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// @Auth 어노테이션은 클라이언트가 로그인된 회원인지 아닌지, 
// 그리고 일반 회원인지 관리자인지 구분할 용도인 어노테이션
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME) // 세션을 처리하기 위한 어노테이션
public @interface Auth {
	
	//public Role role() default Role.USER;
	public String value() default "USER";
	
	//public int test() default 1;
}