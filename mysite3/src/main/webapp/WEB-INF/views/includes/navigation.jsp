<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="navigation">
	<ul>
		<li><a href="${pageContext.servletContext.contextPath}">박준호</a></li>
		<li><a href="${pageContext.servletContext.contextPath}/guestbook/list">방명록</a></li>
		<li><a href="${pageContext.servletContext.contextPath}/guestbook/ajax-list">방명록(ajax)</a></li>
		<li><a href="${pageContext.servletContext.contextPath}/guestbook/ajax-guestbook-list">방명록(ajax2)</a></li>
		<li><a href="${pageContext.servletContext.contextPath}/board/list?page=1">게시판</a></li>
	</ul>
</div>