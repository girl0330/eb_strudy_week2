package com.sb.sbweek3.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentInfoDTO {
    private int commentId; // 댓글 ID
    private int boardId; // 게시글 ID
    private String commentContent; // 댓글 내용
}
