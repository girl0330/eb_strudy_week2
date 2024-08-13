<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>spring boot 게시판 목록</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/board-list.css">
</head>
<body>
<div id="wrap">
    <h2>게시글 리스트</h2>

    <!-- 검색 영역 -->
    <div id="search-box" style="border: 1px solid black; padding: 10px; margin-bottom: 20px;">
        <form action="BoardServlet" method="get">
            <input type="hidden" name="command" value="board_list">

            <!-- 등록일 필터 -->
            <label for="startDate">등록일: </label>
            <input type="text" id="startDate" name="startDate" placeholder="시작일">
            <span>~</span>
            <input type="text" id="endDate" name="endDate" placeholder="종료일">

            <!-- 카테고리 필터 -->
            <label for="category"></label>
            <select id="category" name="category">
                <option value="">전체 카테고리</option>
                <option value="1">카테고리 1</option>
                <option value="2">카테고리 2</option>
                <!-- 다른 카테고리 옵션 추가 가능 -->
            </select>

            <!-- 검색어 입력 -->
            <label for="searchKeyword"></label>
            <input type="text" id="searchKeyword" name="searchKeyword" placeholder="검색어 입력">
            <button type="submit">검색</button>
        </form>
    </div>

    <!-- 총 개수 및 글 작성 버튼 영역 -->
    <div id="info-bar">
        <span id="total-count">총 - 건</span>
        <a href="/post" class="btn">글 작성하기</a>
    </div>

    <table>
        <tr>
            <th>카테고리</th>
            <th>작성자</th>
            <th>제목</th>
            <th>조회수</th>
            <th>작성일</th>
            <th>수정일</th>
        </tr>
        <c:forEach var="list" items="${lists}">
            <tr>
                <td>${list.categoryName }</td>
                <td>${list.writer }</td>
                <td>${list.title }</td>
                <td>${list.viewCount }</td>
                <td>${list.systemRegisterDatetime }</td>
                <td>${list.systemUpdateDatetime }</td>
            </tr>
        </c:forEach>
    </table>
    <div id="pagination">
        <a href="?page=prev" class="page-link">이전</a>
        <a href="?page=1" class="page-link">1</a>
        <a href="?page=2" class="page-link">2</a>
        <a href="?page=3" class="page-link">3</a>
        <a href="?page=4" class="page-link">4</a>
        <a href="?page=5" class="page-link">5</a>
        <a href="?page=6" class="page-link">6</a>
        <a href="?page=7" class="page-link">7</a>
        <a href="?page=8" class="page-link">8</a>
        <a href="?page=9" class="page-link">9</a>
        <a href="?page=10" class="page-link">10</a>
        <a href="?page=next" class="page-link">다음</a>
    </div>
</div>
</body>
</html>
