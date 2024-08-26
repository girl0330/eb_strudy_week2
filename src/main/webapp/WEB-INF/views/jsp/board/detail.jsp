<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판 보기</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/board-detail.css">

</head>
<body>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function() {
        findAllFile();

    });

    // 전체 파일 조회
    function findAllFile() {
        $.ajax({
            url: `/ajax/${detail.boardId}/filesDetail`, // 파일 리스트를 가져오는 엔드포인트
            method: 'GET',
            success: function (response) {
                console.log("파일 확인 :"+JSON.stringify(response));
                renderFileList(response);
            },
            error: function (xhr, status, error) {
                console.error('파일 리스트를 가져오는 데 실패했습니다:', error);
            }
        });
    }
    function renderFileList (response) {
        console.log("???" +JSON.stringify(response));
        const filesList = response;
        const container = $("#fileArea");
        container.empty();

        // 파일 리스트 HTML 생성
        let fileListHtml = '';
        filesList.forEach(file => {
            fileListHtml +=
                `<tr>` +
                `<th scope="row">첨부파일</th>` +
                `<td colspan="3">` +
                `<span class="">` + file.originalFilename + `</span><span class=""></span>` +
                `<a href="/ajax/files/` + file.fileId + `/download" class="" role="button" target="_blank" data-linktype="file" data-linkdata="-">` +
                `<span class="se-blind">파일 다운로드</span>` +
                `</a>` +
                `</td>` +
                `</tr>`;
        });
        container.append(fileListHtml);
    }

</script>

<div id="wrap">
    <h2>게시판 보기</h2>
    <a type="hidden" name="boardId" id="boardId" value="${detail.boardId}"></a>

    <div class="info-section">
        <div class="left">작성자:${detail.writer}</div>
        <div class="right">
            <div>등록일시: ${detail.systemRegisterDatetime}</div>
            <div>수정일시: ${detail.systemUpdateDatetime}</div>
        </div>
    </div>

    <div class="content-section">
        <div class="category-title">
            <div>[${detail.categoryName}] ${detail.title}</div>
            <div>조회수: ${detail.viewCount}</div>
        </div>

        <div class="content">
            ${detail.content}
        </div>

        <div id="fileArea">
            <tr id="fileList">
                <th scope="row">첨부파일</th>
                <td id="files" colspan="3">
                    <!-- 여기에 동적으로 파일 리스트를 추가 -->
                    첨부파일 확인용
                </td>
            </tr>
        </div>
    </div>

    <div class="comments">
        <div class="comment">2020.03.09 16:32 댓글이 출력됩니다.</div>
        <div class="comment">2018.03.09 14:23 댓글이 출력됩니다. 댓글이 출력됩니다. 댓글이 출력됩니다.</div>
    </div>

    <div class="comment-box">
        <textarea placeholder="댓글을 입력해 주세요."></textarea>
        <button class="button">등록</button>
    </div>

    <div class="actions">
        <a href="/board-list" class="button">목록</a>
        <a href="#" class="button" onclick="location.href='/board-update-page?boardId=${detail.boardId}'">수정</a>
        <a href="#" class="button">삭제</a>
    </div>
</div>

</body>
</html>
