package kr.co.itcen.mysite.exception;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.itcen.mysite.dto.JSONResult;

//<context:component-scan base-package="kr.co.itcen.mysite.exception" />
// component-scan base-package 에 경로 추가
@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Log Log = LogFactory.getLog(GlobalExceptionHandler.class );
	
	// 모든 Exception을 다 받아서 처리하고 싶다. @ExceptionHandler(Exception.class)
	@ExceptionHandler(Exception.class)
	public void handlerException(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Exception e) throws Exception {
		
		// 1. 로깅
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		Log.error(errors.toString());
		
		// 2. 요청구분
		// 만약 JSON 요청인 경우는 application/json
		// 만약 html 요청인 경우는 text/html
		// 만약 jpeg 요청인 경우는 image/jpeg
		String accept = request.getHeader("accept");
		if(accept.matches(".*application/json.*")) {
			// 3. json 응답
			response.setStatus(HttpServletResponse.SC_OK); // 200 OK
			
			JSONResult jsonResult =  JSONResult.fail(errors.toString());
			String result = new ObjectMapper().writeValueAsString(jsonResult);
			
			OutputStream os = response.getOutputStream();
			os.write(result.getBytes("utf-8"));
			os.close();
			
		} else {
			// 3. 안내페이지
			request.setAttribute("uri", request.getRequestURI());
			request.setAttribute("exception", errors.toString());
			request
				.getRequestDispatcher("/WEB-INF/views/error/exception.jsp")
				.forward(request, response);
		}
	}
}
