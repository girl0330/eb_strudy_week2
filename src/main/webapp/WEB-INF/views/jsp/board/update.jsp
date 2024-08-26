<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Form Example</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/board-post.css">
</head>
<body>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
 <script>
    // 파일 삭제 처리용 익명 함수
    const removeFileId = (function() {
        const filesIds = [];
        return {
            add(fileId) {
                if (filesIds.includes(fileId)) {
                    return false;
                }
                filesIds.push(fileId);
            },
            getAll() {
                return filesIds;
            }
        }
    }());

    function selectFile(element, fileId) {

        const file = element.files[0];
        const filename = element.closest('.file_input').firstElementChild;

        // 1. 파일 선택 창에서 취소 버튼이 클릭된 경우
        if ( !file ) {
            filename.value = '';
            return false;
        }

        // 2. 파일 크기가 10MB를 초과하는 경우
        const fileSize = Math.floor(file.size / 1024 / 1024);
        if (fileSize > 10) {
            alert('10MB 이하의 파일로 업로드해 주세요.');
            filename.value = '';
            element.value = '';
            return false;
        }

        // 3. 파일명 지정
        filename.value = file.name;

        if(fileId) {
            removeFileId.add(fileId);
        }
    }


    //파일 추가
    function addFile() {
        const fileDiv = document.createElement('div');
        fileDiv.innerHTML =`
            <div class="file_input">
                <input type="text" readonly />
                <label> 첨부파일
                    <input type="file" name="files" onchange="selectFile(this);" />
                </label>
            </div>
            <button type="button" onclick="removeFile(this);" class="btns del_btn"><span>삭제</span></button>
        `;
        document.querySelector('.file_list').appendChild(fileDiv);
    }

    // 파일 삭제
    function removeFile(element, fileId) {
        if(fileId) {
            removeFileId.add(fileId);
        }

        const fileAddBtn = element.nextElementSibling;
        if (fileAddBtn) {
            const inputs = element.previousElementSibling.querySelectorAll('input');
            inputs.forEach(input => input.value = '')
            return false;
        }
        element.parentElement.remove();
    }

    let boardUpdate = {
        init: function () {

            this.updateSubmit()
        },
        updateSubmit: function () {
            const form = document.getElementById('boardUpdateForm');
            const formData = new FormData();

            // 새로운 파일들만 추가
            const fileInputs = form.querySelectorAll('input[type="file"]');
            fileInputs.forEach(input => {
                if (input.files.length > 0) {
                    formData.append('files', input.files[0]);
                }
            });

            // 수정된 제목과 내용 추가
            const titleInput = document.getElementById('title');
            const contentInput = document.getElementById('content');
            formData.append('title', titleInput.value);
            formData.append('content', contentInput.value);


            let deleteFileId = removeFileId.getAll();
            console.log("삭제할 파일 아이디 확인: " +deleteFileId);


            // 삭제할 파일 ID를 각각 FormData에 추가
            deleteFileId.forEach(id => {
                formData.append('deleteFileIds[]', id); // 배열 요소 개별 추가
            });

            // 디버그용으로 FormData 내용 출력
            formData.forEach((value, key) => {
                console.log(key, value);
            });

            $.ajax({
                url: '/ajax/${detail.boardId}/board-update.do',
                type: 'POST',
                data: formData,
                processData: false,  // 데이터의 처리를 방지 (FormData 사용 시 필요)
                contentType: false,  // Content-Type 헤더 설정을 방지 (FormData 사용 시 필요)
                success: function (response) {
                    console.log("파일 확인 :"+JSON.stringify(response));
                    if (response.code === "success") {
                        alert(response.message);
                        location.href="/board-list";
                    }
                },
                error: function (xhr, status, error) {
                    console.error('파일 리스트를 가져오는 데 실패했습니다:', error);
                }
            });
        }

    }

    $(document).ready(function () {
        $("#update-button").on("click", function () {
            alert("버튼 클릭");

            boardUpdate.init();

        })
    })
</script>
<div id="wrap">
    <h2>게시판 등록</h2>
    <a type="hidden" name="boardId" id="boardId" value="${detail.boardId}"></a>

    <form id="boardUpdateForm" autocomplete="off" enctype="multipart/form-data">
        <table>
            <tr>
                <td class="label">카테고리 *</td>
                <td class="input-field">
                    <select id="categoryId" name="categoryId" data-name="카테고리" disabled value="">
                        <option value="0" ${detail.categoryId == 0 ? 'selected' : ''}>카테고리 선택</option>
                        <option value="1" ${detail.categoryId == 1 ? 'selected' : ''}>Java</option>
                        <option value="3" ${detail.categoryId == 3 ? 'selected' : ''}>Python</option>
                        <option value="4" ${detail.categoryId == 4 ? 'selected' : ''}>JavaScript</option>
                        <option value="5" ${detail.categoryId == 5 ? 'selected' : ''}>HTML/CSS</option>
                        <option value="6" ${detail.categoryId == 6 ? 'selected' : ''}>Django</option>
                        <option value="7" ${detail.categoryId == 7 ? 'selected' : ''}>Flask</option>
                        <option value="8" ${detail.categoryId == 8 ? 'selected' : ''}>Spring Boot</option>
                        <option value="9" ${detail.categoryId == 9 ? 'selected' : ''}>React.js</option>
                        <option value="10" ${detail.categoryId == 10 ? 'selected' : ''}>Vue.js</option>
                        <option value="11" ${detail.categoryId == 11 ? 'selected' : ''}>Node.js</option>
                        <option value="12" ${detail.categoryId == 12 ? 'selected' : ''}>Angular</option>
                        <option value="13" ${detail.categoryId == 13 ? 'selected' : ''}>Servlet</option>
                    </select>
                </td>
            </tr>

            <tr>
                <td class="label">작성자 *</td>
                <td class="input-field">
                    <input type="text" value="${detail.writer}" id="writer" name="writer" data-name="작성자" disabled>
                </td>
            </tr>

            <tr>
                <td class="label">비밀번호 *</td>
                <td class="input-field">
                    <div class="password-container">
                        <input type="password" value="${detail.password}" id="password" name="password" placeholder="비밀번호" data-name="비밀번호" disabled>
                        <input type="password" value="${detail.password}" id="password-confirm" name="password-confirm" placeholder="비밀번호 확인" data-name="비밀번호 확인" disabled>
                    </div>
                </td>
            </tr>

            <tr>
                <td class="label">제목 *</td>
                <td class="input-field">
                    <input type="text" value="${detail.title}" id="title" name="title" data-name="제목">
                </td>
            </tr>

            <tr>
                <td class="label">내용 *</td>
                <td class="input-field">
                    <textarea id="content" name="content" data-name="내용">${detail.content}</textarea>
                </td>
            </tr>
            <tr>
                <th scope="row">첨부파일</th>
                <td id="files" colspan="3">
                    <div class="file_list">
                        <div>
                            <c:forEach var="file" items="${fileInfo}">
                            <%--서버에서 넘어온 파일 데이터의 수만큼 보여줌 --%>
                                <div class="file_input">
                                    <input type="text" value="${file.originalFilename}" readonly />
                                     <a type="hidden" id="fileId" value="${file.fileId}"></a>
                                    <label> 첨부파일
                                        <input type="file" name="files" onchange="selectFile(this, ${file.fileId});" />
                                    </label>
                                </div>
                                <button type="button" onclick="removeFile(this, ${file.fileId});" class="btns del_btn"><span>삭제</span></button>
                            </c:forEach>
                            <button type="button" onclick="addFile();" class="btns fn_add_btn"><span>파일 추가</span></button>
                        </div>
                    </div>
                </td>
            </tr>
        </table>
    </form>
    <tr>
        <td colspan="2">
            <div class="form-actions">
                <a href="#" class="button cancel" id="cancel-button">취소</a>
                <a href="/board-list" class="button" id="go-list-button">목록</a>
                <a href="#" class="button" id="update-button">저장</a>
            </div>
        </td>
    </tr>
</div>
</body>
</html>