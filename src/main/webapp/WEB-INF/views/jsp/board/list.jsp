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
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    const keywordSearch = {
        init : function () {
            this.keywordSearchSubmit();
        },

        keywordSearchSubmit: function () {
            const formData = $("#searchForm").serialize();

            $.ajax({
                url: '/ajax/search/board-list.do',  // 서버의 엔드포인트 URL
                type: 'GET',
                data: formData,
                success: (response) => {
                    console.log("검색 결과 리스트:"+JSON.stringify(response));
                    keywordSearch.renderSearchList(response);
                },
                error: function(xhr, status, error) {
                    // 서버에서 받은 JSON 오류 응답 처리
                    let errorMessage = xhr.responseJSON ? xhr.responseJSON.message : "알 수 없는 오류가 발생했습니다.";
                    console.error('파일 업로드 실패:', errorMessage);

                    // 사용자에게 오류 메시지 표시
                    alert("오류 발생: " + errorMessage);
                }
            });
        },

        renderSearchList: function (response) {
            const searchListsTotal = response.searchListsTotal;
            const searchLists = response.searchLists;
            const container = $("#listArea");
            container.empty();

            let searchListHtml =
                `<div id="info-bar">` +
                `<span id="total-count">총` + searchListsTotal + `건</span>` +
                `<a href="/board-post-page" class="btn">글 작성하기</a>` +
                `</div>` +
                `<table id="data-table">` +
                `<thead>` +
                `<tr>` +
                `<th>카테고리</th>` +
                `<th>작성자</th>` +
                `<th>제목</th>` +
                `<th>조회수</th>` +
                `<th>작성일</th>` +
                `<th>수정일</th>` +
                `</tr>` +
                `</thead>` +
                `<tbody>`;

            searchLists.forEach(item => {
                searchListHtml +=
                    `<tr>` +
                        `<td>` + item.categoryName + `</td>` +
                        `<td>` + item.writer + `</td>` +
                        `<td><a href="/board-detail-page?boardId= + item.boardId + ">` + item.title + `</a></td>` +
                        `<td>` + item.viewCount + `</td>` +
                        `<td>` + item.systemRegisterDatetime + `</td>` +
                        `<td>` + item.systemUpdateDatetime + `</td>` +
                    `</tr>`;
            });

            searchListHtml +=
                `</tbody>` +
                `</table>`;

            container.append(searchListHtml);
        }
    };

    let goDetail = (event) => {
        // 클릭된 링크의 부모 <td> 요소를 찾습니다.
        let parentTd = $(event.target).closest('td');

        // 부모 <td> 내의 <input> 요소를 찾고, 그 값을 가져옵니다.
        let boardId = parentTd.find('#boardId').val();

        window.location.href = "/board-detail-page?boardId=" + boardId + "&viewSet=yes";
    }

    $(document).ready(function() {
        $('#searchButton').on('click', function (e) {
            alert("???")
            e.preventDefault();
            keywordSearch.init();
        });
    });
</script>
<div id="wrap">
    <h2>게시글 리스트</h2>

    <!-- 검색 영역 -->
    <div id="search-box" style="border: 1px solid black; padding: 10px; margin-bottom: 20px;">
        <form id="searchForm" name="searchForm">

            <!-- 등록일 필터 -->
            <label for="startDate">등록일: </label>
            <input type="text" id="startDate" name="startDate" placeholder="시작일" data-valid="true">
            <span>~</span>
            <input type="text" id="endDate" name="endDate" placeholder="종료일" data-valid="true">

            <!-- 카테고리 필터 -->
            <label for="categoryId"></label>
            <select id="categoryId" name="categoryId" data-valid="true">
                <option value="0">카테고리 선택</option>
                <!-- categoryLists를 반복하여 옵션을 생성 -->
                <c:forEach var="categoryList" items="${categoryLists}">
                    <option value="${categoryList.categoryId}">${categoryList.category}</option>
                </c:forEach>
            </select>

            <!-- 검색어 입력 -->
            <label for="searchKeyword"></label>
            <input type="text" id="searchKeyword" name="searchKeyword" placeholder="검색어 입력" data-valid="true">
            <button type="button" id="searchButton">검색</button>
        </form>
    </div>

    <div id="listArea" class="listArea">
        <!-- 총 개수 및 글 작성 버튼 영역 -->
        <div id="info-bar">
            <span id="total-count">총 ${total} 건</span>
            <a href="/board-post-page" class="btn">글 작성하기</a>
        </div>

        <table id="data-table">
            <thead>
            <tr>
                <th>카테고리</th>
                <th>작성자</th>
                <th>제목</th>
                <th>조회수</th>
                <th>작성일</th>
                <th>수정일</th>
            </tr>
            </thead>
            <tbody>
            <!-- 데이터가 여기에 삽입됩니다 -->
            <c:forEach var="list" items="${list}">
                <tr>
                    <td>${list.category}</td>
                    <td>${list.writer}</td>
                    <td>
                        <input type="hidden" id="boardId" value="${list.boardId}">
                        <a href="#" id="detail-board" onclick="goDetail(event)"> ${list.title} </a>
                    </td>
                    <td>${list.viewCount}</td>
                    <td>${list.systemRegisterDatetime}</td>
                    <td>${list.systemUpdateDatetime}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div id="pagination">
        <!-- 이전 페이지 링크 -->
        <c:if test="${pageInfoDTO.currentPage > 1}">
            <a href="?page=${pageInfoDTO.previousPage}" class="page-link">이전</a>
        </c:if>

        <!-- 페이지 번호 링크 -->
        <c:forEach var="i" begin="${pageInfoDTO.startPageIndex}" end="${pageInfoDTO.endPageIndex}">
            <a href="?page=${i}" class="page-link ${pageInfoDTO.currentPage == i ? 'active' : ''}">${i}</a>
        </c:forEach>

        <!-- 다음 페이지 링크 -->
        <c:if test="${pageInfoDTO.currentPage < pageInfoDTO.pageTotal}">
            <a href="?page=${pageInfoDTO.nextPage}" class="page-link">다음</a>
        </c:if>
    </div>
</div>
</body>
</html>
