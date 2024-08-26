package com.sb.sbweek3.category;

import com.sb.sbweek3.dto.CategoryInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<CategoryInfoDTO> getCategoryList();
}
