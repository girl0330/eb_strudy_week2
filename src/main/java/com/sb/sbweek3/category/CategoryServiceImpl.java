package com.sb.sbweek3.category;

import com.sb.sbweek3.dto.CategoryInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl {

    private final CategoryMapper categoryMapper;
    public List<CategoryInfoDTO> getCategoryList() {
        return categoryMapper.getCategoryList();
    }
}
