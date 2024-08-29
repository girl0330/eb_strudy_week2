package com.sb.sbweek3.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class BoardInfoDTO {
    private int boardId; // 게시글 ID
    private int categoryId; // 카테고리 ID
    private String categoryName; //카테고리 이름
    private String category; // 카테고리 이름
    private String writer; // 작성자
    private int password; // 비밀번호
    private String title; // 제목
    private String content; // 내용
    private int viewCount; // 조회수
    private String systemRegisterDatetime; // 등록일시
    private String systemUpdateDatetime; // 수정일시
    private String viewSet = "no";//조회수 즈가

    private List<MultipartFile> files = new ArrayList<>(); // 첨부파일 List
    private List<Integer> deleteFileIds = new ArrayList<>(); // 삭제할 첨부파일 fileId List
//    private int file_id;
//    private String original_filename;
//    private String saved_filename;
//    private String file_path;
//    private String file_type;
//    private int file_size;
}
