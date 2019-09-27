<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.servletContext.contextPath}/assets/css/board.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="board">
				<form class="board-form" method="post" action="${pageContext.servletContext.contextPath}/board" enctype="multipart/form-data">
					<input type="hidden" name='a'  value='reply' />
					<input type="hidden" name='no' value='${param.no}' />
					<input type="hidden" name='groupno' value='${vo.groupNo}' />
					<input type="hidden" name='orderno' value='${vo.orderNo}' />
					<input type="hidden" name='depth' value='${vo.depth}' />
					<table class="tbl-ex">
						<tr>
							<th colspan="2">답글쓰기</th>
						</tr>
						<tr>
							<td class="label">제목</td>
							<td><input type="text" name="title" value=""></td>
						</tr>
						<tr>
							<td class="label">내용</td>
							<td>
								<textarea id="contents" name="contents" rows="10" cols="70"></textarea>
							</td>
						</tr>
						<tr>
							<td class="label">파일 업로드</td>
							<td>
								<input type="file" name="file">
							</td>
						</tr>
					</table>
					<div class="bottom">
						<a href="${pageContext.servletContext.contextPath}/board?a=list">취소</a>
						<input type="submit" value="답글등록">
					</div>
				</form>				
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>