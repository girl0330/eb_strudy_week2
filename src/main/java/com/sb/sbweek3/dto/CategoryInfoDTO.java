package com.sb.sbweek3.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryInfoDTO {
    private int categoryId; // 카테고리 ID
    private String category; // 카테고리 이름
}
