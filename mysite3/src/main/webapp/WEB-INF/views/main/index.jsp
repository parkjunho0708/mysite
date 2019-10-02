<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.servletContext.contextPath}/assets/css/main.css" rel="stylesheet" type="text/css">
<link href="${pageContext.servletContext.contextPath}/assets/css/slide.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/assets/js/gallery.js"></script>
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="wrapper">
			<div id="content">
				<div id="site-introduction">
					<h2>안녕하세요. 박준호의  mysite에 오신 것을 환영합니다.</h2>
					<p>
						이 사이트는  웹 프로그램밍 실습과제 예제 사이트입니다.<br>
						메뉴는  사이트 소개, 방명록, 게시판이 있구요. Python 수업 + 데이터베이스 수업 + 웹프로그래밍 수업 배운 거 있는거 없는 거 다 합쳐서
						만들어 놓은 사이트 입니다.<br><br>
					</p>
				</div>
				<div id="site-introduction">
					<div class="slideshow-container">
						<div class="mySlides fade">
							<div class="numbertext">1 / 3</div>
							<img src="${pageContext.servletContext.contextPath}/assets/images/yuna.jpg" style="width: 100%">
							<div class="text">Yuna Kim</div>
						</div>
						<div class="mySlides fade">
							<div class="numbertext">2 / 3</div>
							<img src="${pageContext.servletContext.contextPath}/assets/images/akmu.jpg" style="width: 100%">
							<div class="text">AKMU</div>
						</div>
						<div class="mySlides fade">
							<div class="numbertext">3 / 3</div>
							<img src="${pageContext.servletContext.contextPath}/assets/images/jisung.jpeg" style="width: 100%">
							<div class="text">Jisung Park</div>
						</div>
					</div>
					<br>
					<div style="text-align: center">
						<span class="dot"></span>
						<span class="dot"></span>
						<span class="dot"></span>
					</div>
				</div>
				<div id="site-introduction">
				<input type="button" value="Select All" onclick="selectAll(this, event);">
				<input type="button" value="Play Slide Show" onclick="slideShow(this, event);">
				<hr>
				
				</div>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/assets/js/slide.js"></script>
</html>