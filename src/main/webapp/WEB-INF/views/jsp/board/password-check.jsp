<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <h2> 비밀번호 확인 </h2>
</head>

<body>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function() {
        $("#password-check-button").on("click", function check () {
                const password = $('#password').val();
                const boardId = $('#boardId').val();

                $.ajax({
                    type: "post",
                    url: "/ajax/delete-check.do",
                    data: {
                        password: password,
                        boardId: boardId
                    },
                    dataType: "json",
                    success: function(data) {
                        if (data.statusCode === 200) {
                            if (data.redirectUrl) {
                                alert(data.message);
                                window.location.href = data.redirectUrl;
                            }
                        } else if (data.statusCode === 401){
                            // 오류 처리
                            alert(data.message);
                        }
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        console.error('Error:', textStatus, errorThrown);
                    }
                });
        })
    });
</script>
    <div class="password">
        <tbody>
        <tr>
            <td>비밀번호</td>
            <input type="hidden" id="boardId" value=${boardId}>
            <td>
                <input type="text" id="password" name="password">
            </td>
        </tr>
        </tbody>
        <button id="cancel-button" >취소</button>
        <button id="password-check-button">확인</button>
    </div>
</body>
</html>
