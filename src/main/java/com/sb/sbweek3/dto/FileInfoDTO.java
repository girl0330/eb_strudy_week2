package com.sb.sbweek3.dto;

import lombok.*;

@Data
@Builder
public class FileInfoDTO {
    private int fileId; // 파일 ID
    private int boardId; // 게시글 번호
    private String originalFilename; // 원본 파일명
    private String savedFilename; // 저장된 파일명
    private String filePath; // 파일 경로
    private String fileType; // 파일 유형
    private long fileSize; // 파일 크기

    private boolean deleteYn; // 저장된 파일 삭제여부
    private String systemRegisterDatetime; // 작성일시
    private String systemUpdateDatetime; // 수정일시
    private String fileDeletedDatetime; // 삭제일시

}
