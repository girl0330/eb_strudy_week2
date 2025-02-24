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
    function selectFile(element) {
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
    }


    // 파일 추가
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
    function removeFile(element) {
        const fileAddBtn = element.nextElementSibling;
        if (fileAddBtn) {
            const inputs = element.previousElementSibling.querySelectorAll('input');
            inputs.forEach(input => input.value = '')
            return false;
        }
        element.parentElement.remove();
    }

    let boardSave = {
        init: function () {
            //공백 검사
            // if (this.emptyCheck()) { //true이면 고백임.
            //     alert("공백임!");
            //     return;
            // }
            // // 유효성 검사
            // if (!this.validationCheck()) {
            //     alert("유효성 검사 실패");
            //     return;
            // }
            this.formSubmit();
        },
        // 공백 검사
        emptyCheck: function () {
            let valid = false;
            const writer = $('#writer').val();
            const password = $('#password').val();
            const title = $('#title').val();
            const content = $('#content').val();
            const removeBlank = comment.replace(/\s*/g, "");

            if (removeBlank === "") {
                let text = $('#commentContent').data('name');
                alert(text + "이 비여있습니다.");
                $('#commentContent').focus();
                valid = true;
            }
            return valid;
        },
        // 유효성 검사
        validationCheck: function () {
            let valid = true;
            const writer = $('#writer').val();
            const password = $('#password').val();
            const title = $('#title').val();
            const content = $('#content').val();

        },

        //전송 함수 정의
        formSubmit: function () {
            const formElement = document.getElementById('boardSaveForm');
            const formData = new FormData(formElement); // 폼 데이터를 포함한 FormData 객체 생성

            formData.forEach((value, key) => {
                console.log(key, value);
            });

            $.ajax({
                url: '/ajax/board-save.do',  // 서버의 엔드포인트 URL
                type: 'POST',
                data: formData,
                processData: false,  // 데이터의 처리를 방지 (FormData 사용 시 필요)
                contentType: false,  // Content-Type 헤더 설정을 방지 (FormData 사용 시 필요)
                success: function(data) {
                    console.log('파일 업로드 성공:', data);
                    if (data.statusCode === 200) {
                        if (data.redirectUrl) {
                            alert(data.message);
                            window.location.href = data.redirectUrl;
                        }
                    }
                },
                error: function(xhr, status, error) {
                    // 서버에서 받은 JSON 오류 응답 처리
                    let errorMessage = xhr.responseJSON ? xhr.responseJSON.message : "알 수 없는 오류가 발생했습니다.";
                    console.error('파일 업로드 실패:', errorMessage);

                    // 사용자에게 오류 메시지 표시
                    alert("오류 발생: " + errorMessage);
                }
            });

        }
    }

    $(document).ready(function () {
        document.getElementById("save-button").addEventListener("click", function () {
            alert("버튼 클릭")
            boardSave.init();
        })
    })
</script>

<div id="wrap">
    <h2>게시판 등록</h2>

    <form id="boardSaveForm" method="post" autocomplete="off" enctype="multipart/form-data">
        <table>
            <tr>
                <td class="label">카테고리 *</td>
                <td class="input-field">
                    <select id="categoryId" name="categoryId" value="" data-name="카테고리">
                        <option value="0">카테고리 선택</option>
                        <c:forEach var="category" items="${category}">
                            <option value="${category.categoryId}">${category.category}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>

            <tr>
                <td class="label">작성자 *</td>
                <td class="input-field">
                    <input type="text" value="" id="writer" name="writer" data-name="작성자">
                </td>
            </tr>

            <tr>
                <td class="label">비밀번호 *</td>
                <td class="input-field">
                    <div class="password-container">
                        <input type="password" value="" id="password" name="password" placeholder="비밀번호" data-name="비밀번호">
                        <input type="password" value="" id="password-confirm" name="password-confirm" placeholder="비밀번호 확인" data-name="비밀번호 확인">
                    </div>
                </td>
            </tr>

            <tr>
                <td class="label">제목 *</td>
                <td class="input-field">
                    <input type="text" value="" id="title" name="title" data-name="제목">
                </td>
            </tr>

            <tr>
                <td class="label">내용 *</td>
                <td class="input-field">
                    <textarea id="content" name="content" data-name="내용"></textarea>
                </td>
            </tr>
            <tr>
                <td class="label">첨부파일</td>
                <td colspan="3">
                    <div class="file_list">
                        <div>
                            <div class="file_input">
                                <input type="text" readonly />
                                <label> 첨부파일
                                    <input type="file" name="files" onchange="selectFile(this);" />
                                </label>
                            </div>
                            <button type="button" onclick="removeFile(this);" class="btns del_btn"><span>삭제</span></button>
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
                <a href="#" class="button" id="save-button">저장</a>
            </div>
        </td>
    </tr>
</div>
</body>
</html>


