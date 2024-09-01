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
            // 유효성 검사
            if (!this.validationCheck()) {
                alert("유효성 검사 실패");
                return;
            }
            this.keywordSearchSubmit();
        },
        // 유효성 검사 특수문자 사용 금지
        validationCheck: function () {
            let valid = true;
            const startDate = $('#startDate').val();
            const endDate = $('#endDate').val();
            const searchKeyword = $('#searchKeyword').val();

            let specialCharPattern = /[.!@#$%^&*]+/;
            if (specialCharPattern.test(searchKeyword)) {
                alert("검색 키워드는 특수 문자를 사용할 수 없습니다.")
                $('#searchKeyword').focus();
                valid = false;
                return valid;
            }
            let invalidCharPattern = /[a-zA-Zㄱ-ㅎ가-힣]+/;
            if (invalidCharPattern.test(startDate)) {
                alert("시작일에는 영어 또는 한글을 사용할 수 없습니다.");
                $('#startDate').focus();
                valid = false;
                return valid;
            }
            if (invalidCharPattern.test(endDate)) {
                alert("종료일에는 영어 또는 한글을 사용할 수 없습니다.");
                $('#endDate').focus();
                valid = false;
                return valid;
            }
            return valid;
        },

        keywordSearchSubmit: function () {
            const $form = $('#searchForm');

            $form.find('input[type="text"]').each(function() {
                const $input = $(this);
                $input.val($input.val().trim());
            });

            const formData = $form.serialize();

            // 검색 시 새로운 쿼리스트링을 사용하여 페이지 이동
            window.location.href = "/board-list?" + formData;
        }
    };

    let navigatePage = (page) => {
        // 현재 URL의 쿼리스트링을 가져옵니다.
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);

        // 현재 쿼리스트링에서 "page" 파라미터를 설정합니다.
        urlParams.set('page', page);

        // 쿼리스트링을 유지하면서 페이지를 이동합니다.
        window.location.href = "/board-list?" + urlParams.toString();
    }

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
            <input type="text" id="startDate" name="startDate" placeholder="시작일" data-valid="true" value="${search.startDate}">
            <span>~</span>
            <input type="text" id="endDate" name="endDate" placeholder="종료일" data-valid="true" value="${search.endDate}">

            <label for="categoryId">카테고리: </label>
            <select id="categoryId" name="categoryId" data-valid="true">
                <option value="0">카테고리 선택</option>
                <!-- categoryLists를 반복하여 옵션을 생성 -->
                <c:forEach var="category" items="${category}">
                    <option value="${category.categoryId}" ${search.categoryId == category.categoryId ? 'selected' : ''}>${category.category}</option>
                </c:forEach>
            </select>

            <label for="searchKeyword">검색어: </label>
            <input type="text" id="searchKeyword" name="searchKeyword" placeholder="검색어 입력" data-valid="true" value="${search.searchKeyword}">
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
        <c:choose>
            <c:when test="${paging.page > 1}">
                <a href="javascript:void(0)" onclick="navigatePage(${paging.page-1})">[이전]</a>
            </c:when>
        </c:choose>

        <c:forEach begin="${paging.startPage}" end="${paging.endPage}" var="i" step="1">
            <c:choose>
                <c:when test="${i eq paging.page}">
                    <span>${i}</span>
                </c:when>
                <c:otherwise>
                    <a href="javascript:void(0)" onclick="navigatePage(${i})">${i}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:choose>
            <c:when test="${paging.page < paging.maxPage}">
                <a href="javascript:void(0)" onclick="navigatePage(${paging.page+1})">[다음]</a>
            </c:when>
        </c:choose>
    </div>
</div>
</body>
</html>
