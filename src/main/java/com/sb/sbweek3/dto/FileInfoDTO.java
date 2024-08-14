package com.sb.sbweek3.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileInfoDTO {
    private int fileId; // 파일 ID
    private int boardId; // 게시글 번호
    private String originalFilename; // 원본 파일명
    private String savedFilename; // 저장된 파일명
    private String filePath; // 파일 경로
    private String fileType; // 파일 유형
    private long fileSize; // 파일 크기
    /**
    private boolean deleteYn; // 저장된 파일 삭제여부
    private String systemRegisterDatetime; // 작성일시
    private String systemUpdateDatetime; // 수정일시
    private String fileDeletedDatetime; // 삭제일시
    */

    @Builder
    public FileInfoDTO(String originalFilename, String savedFilename, String filePath,String fileType, Long fileSize) {
        this.originalFilename = originalFilename;
        this.savedFilename = savedFilename;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }
}
