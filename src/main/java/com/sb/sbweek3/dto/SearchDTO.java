package com.sb.sbweek3.dto;

import lombok.Data;

@Data
public class SearchDTO {
    private String searchKeyword; // 검색어
    private int categoryId; // 검색 카테고리 ID
    private String startDate; // 시작일
    private String endDate; // 종료일

}
