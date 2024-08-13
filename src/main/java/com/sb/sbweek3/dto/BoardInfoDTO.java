package com.sb.sbweek3.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardInfoDTO {
    private int boardId; // 게시글 ID
    private int categoryId; // 카테고리 ID
    private String writer; // 작성자
    private int password; // 비밀번호
    private String title; // 제목
    private String content; // 내용
    private int viewCount; // 조회수
    private String systemRegisterDatetime; // 등록일시
    private String systemUpdateDatetime; // 수정일시
}
