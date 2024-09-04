package com.sb.sbweek3.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    PASSWORD_MISMATCH_TOKEN(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_BOARD_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 게시판 ID입니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    PASSWORD_INCORRECT_TOKEN(HttpStatus.UNAUTHORIZED,"비밀번호가 존재하지 않거나, 일치하지 않습니다."),

    /* 403 FORBIDDEN : 서버가 요청을 이해했지만, 권한이 없어서 요청을 거부*/

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 페이지 정보를 찾을 수 없습니다"),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시판을 찾을 수 없습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */

    /* 500 INTERNAL_SERVER_ERROR : 서버 오류 */
    GENERIC_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다"),
    FILE_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장 중 오류가 발생했습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 게시글을 찾을 수 없습니다."),
    NULL_POINTER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "null 값을 입력했습니다."),


    EXCEPTION_MESSAGE(HttpStatus.BAD_REQUEST,"");

    private final HttpStatus httpStatus;
    private final String detail;
}
