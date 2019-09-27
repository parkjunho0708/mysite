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
		<c:import url="/WEB-INF/views/includes/header.jsp"/>
		<div id="content">
			<div id="board">
				<form id="search_form" action="${pageContext.servletContext.contextPath}/board/searchlist/1" method="post">
					<input type="text" id="kwd" name="kwd">
					<input type="submit" value="찾기">
				</form>
				<c:set var="count" value="${fn:length(list)}" />
				<c:forEach items="${list}" var="vo" varStatus="status">
				
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>				
					<tr>
						<td>${vo.no}</td>
						<c:if test="${vo.depth > 0}">
							<td style='padding-left:${40 * vo.depth}px'>
								<img src='${pageContext.servletContext.contextPath }/assets/images/reply.png'/>
								<a href="${pageContext.servletContext.contextPath}/board/view?no=${vo.no}&username=${vo.userName}">${vo.title}</a>
							</td>
						</c:if>
						<c:if test="${vo.depth == 0}">
							<td>
								<a href="${pageContext.servletContext.contextPath}/board/view?no=${vo.no}&username=${vo.userName}">${vo.title}</a>
							</td>
						</c:if>
						<td>${vo.userName}</td>
						<td>${vo.hit}</td>
						<td>${vo.regDate}</td>
						<c:if test="${authUser.no == vo.userNo}">
							<a href="${pageContext.servletContext.contextPath}/board?a=deleteform&no=${vo.no}" class="del">삭제</a>
						</c:if>
					</tr>
				</table>
				</c:forEach>
				
				<div class="pager">
					<%--Page 이전 페이지 구현 --%>
					<ul> 
						<c:choose>
							
							<%-- all data list pagination --%>
							<c:when test="${kwd == null && pageInfo.totalRows != 0}">
								<c:choose>
									<c:when test="${pageInfo.currentBlock eq 1}"><li>◀</li></c:when>
									<c:otherwise>
										<li><a href="${pageContext.servletContext.contextPath}/board/list?&page=${(pageInfo.currentBlock-1)*pageInfo.pagesPerBlock}">◀</a></li>
									</c:otherwise>
								</c:choose>

								<%--Page  페이지 구현 --%>
								<c:choose>
									<%-- 첫 페이지 출력 ex) 1 2 3 4 5
									currentBlock : 현재 전체 블럭 --%>
									<c:when test="${pageInfo.currentBlock ne pageInfo.totalBlocks}">
										<c:forEach begin="1" end="${pageInfo.pagesPerBlock}" varStatus="num">
											<c:choose>
												<c:when test="${num.count == pageInfo.currentPage}">
													<li class="selected"><a href="${pageContext.servletContext.contextPath}/board/list?page=${(pageInfo.currentBlock - 1) * pageInfo.pagesPerBlock + num.count}">${(pageInfo.currentBlock- 1) * pageInfo.pagesPerBlock + num.count}</a></li>
												</c:when>
												<c:otherwise>
													<li><a href="${pageContext.servletContext.contextPath}/board/list?page=${(pageInfo.currentBlock - 1) * pageInfo.pagesPerBlock + num.count}">${(pageInfo.currentBlock- 1) * pageInfo.pagesPerBlock + num.count}</a></li>
												</c:otherwise>
											</c:choose>
	                       				</c:forEach>
									</c:when>
									<%-- 첫 페이지 이후의 모든 페이지 출력 ex) 6 7 8 9 10 
																									  11 12 13 14 15 
																									  16 17 18 19 20  totalBlocks : 모두 출력되어야 하는 블럭의 수 --%>
									<c:otherwise>
										<c:forEach begin="${(pageInfo.currentBlock - 1) * pageInfo.pagesPerBlock + 1}" end="${pageInfo.totalPages}" varStatus="num" var="i">
											<c:choose>
												<c:when test="${i == pageInfo.currentPage}">
													<li class="selected"><a href="${pageContext.servletContext.contextPath}/board/list?page=${(pageInfo.currentBlock - 1) * pageInfo.pagesPerBlock + num.count}">${(pageInfo.currentBlock- 1) * pageInfo.pagesPerBlock + num.count}</a></li>
												</c:when>
												<c:otherwise>
													<li><a href="${pageContext.servletContext.contextPath}/board/list?page=${(pageInfo.currentBlock - 1) * pageInfo.pagesPerBlock + num.count}">${(pageInfo.currentBlock- 1) * pageInfo.pagesPerBlock + num.count}</a></li>
												</c:otherwise>
											</c:choose>
	                   					 </c:forEach>
									</c:otherwise>
								</c:choose>

								<%--Page 다음 페이지 구현 --%>
								<c:choose>
									<c:when test="${pageInfo.currentBlock eq pageInfo.totalBlocks}"><li>▶</li></c:when>
									<c:otherwise>
										<li><a href="${pageContext.servletContext.contextPath}/board/list?page=${pageInfo.currentBlock * pageInfo.pagesPerBlock + 1 }">▶</a></li>
									</c:otherwise>
								</c:choose>
							</c:when>
							
							
							<%-- search list pagination --%>
							<c:when test="${kwd != null}">
								<c:choose>
									<c:when test="${pageInfo.currentBlock eq 1}"><li>◀</li></c:when>
									<c:otherwise>
										<li><a href="${pageContext.servletContext.contextPath}/board?a=searchlist&page=${(pageInfo.currentBlock-1)*pageInfo.pagesPerBlock }">◀</a></li>
									</c:otherwise>
								</c:choose>

								<%--Page  페이지 구현 --%>
								<c:choose>
									<c:when test="${pageInfo.currentBlock ne pageInfo.totalBlocks}">
										<c:forEach begin="1" end="${pageInfo.pagesPerBlock}" varStatus="num">
											<c:choose>
												<c:when test="${num.count == pageInfo.currentPage}">
													<li class="selected"><a href="${pageContext.servletContext.contextPath}/board?a=searchlist&page=${(pageInfo.currentBlock - 1) * pageInfo.pagesPerBlock + num.count}&kwd=${kwd}">${(pageInfo.currentBlock- 1) * pageInfo.pagesPerBlock + num.count}</a></li>
												</c:when>
												<c:otherwise>
													<li><a href="${pageContext.servletContext.contextPath}/board?a=searchlist&page=${(pageInfo.currentBlock - 1) * pageInfo.pagesPerBlock + num.count}&kwd=${kwd}">${(pageInfo.currentBlock- 1) * pageInfo.pagesPerBlock + num.count}</a></li>
												</c:otherwise>
											</c:choose>
	                       				</c:forEach>
									</c:when>
									<c:otherwise>
										<c:forEach begin="${(pageInfo.currentBlock - 1) * pageInfo.pagesPerBlock + 1}" end="${pageInfo.totalPages}" varStatus="num" var="i">
											<c:choose>
												<c:when test="${i == pageInfo.currentPage}">
													<li class="selected"><a href="${pageContext.servletContext.contextPath}/board?a=searchlist&page=${(pageInfo.currentBlock - 1) * pageInfo.pagesPerBlock + num.count}&kwd=${kwd}">${(pageInfo.currentBlock- 1) * pageInfo.pagesPerBlock + num.count}</a></li>
												</c:when>
												<c:otherwise>
													<li><a href="${pageContext.servletContext.contextPath}/board?a=searchlist&page=${(pageInfo.currentBlock - 1) * pageInfo.pagesPerBlock + num.count}&kwd=${kwd}">${(pageInfo.currentBlock- 1) * pageInfo.pagesPerBlock + num.count}</a></li>
												</c:otherwise>
											</c:choose>
	                   					 </c:forEach>
									</c:otherwise>
								</c:choose>

								<%--Page 다음 페이지 구현 --%>
								<c:choose>
									<c:when test="${pageInfo.currentBlock eq pageInfo.totalBlocks}"><li>▶</li></c:when>
									<c:otherwise>
										<li><a href="${pageContext.servletContext.contextPath}/board?a=searchlist&page= ${pageInfo.currentBlock * pageInfo.pagesPerBlock + 1 }">▶</a></li>
									</c:otherwise>
								</c:choose>
								
							</c:when>
						</c:choose>
					</ul>
				</div>
				
				<c:if test="${!empty authUser}">
					<div class="bottom">
						<a href="${pageContext.servletContext.contextPath}/board/write" id="new-book">글쓰기</a>
					</div>	
				</c:if>			
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp">
			<c:param name="menu" value="board"/>
		</c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp"/>
	</div>
</body>
</html>