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
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="wrapper">
			<div id="content">
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
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/assets/js/slide.js"></script>
</html>