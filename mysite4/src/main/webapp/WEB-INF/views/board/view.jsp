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
			<div id="board" class="board-form">
				<table class="tbl-ex">
					<tr>
						<th colspan="2">글보기</th>
					</tr>
					<tr>
						<td class="label">제목</td>
						<td>${vo.title}</td>
					</tr>
					<tr>
						<td class="label">내용</td>
						<td>
							<% pageContext.setAttribute("newLineChar", "\n"); %>
							<div class="view-content">
								${fn:replace(vo.contents, newLineChar, "<br/>")}
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">파일 이름</td>
						<td>
							<a href="${pageContext.servletContext.contextPath}/board/download?filename=${vo.filename}">${vo.filename}</a>
						</td>
					</tr>
				</table>
				<div class="bottom">
					<a href="${pageContext.servletContext.contextPath}/board/reply?no=${param.no}">답글</a>
					<a href="${pageContext.servletContext.contextPath}/board/list?page=1">글목록</a>
					${authUser.no}
					${vo.userNo}
					<c:if test="${authUser.no == vo.userNo}">
						<a href="${pageContext.servletContext.contextPath}/board/modify?no=${param.no}&username=${vo.userName}">글수정</a>
					</c:if>
				</div>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>