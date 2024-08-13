package com.sb.sbweek3.domain;

import com.sb.sbweek3.dto.BoardInfoDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {
    BoardInfoDTO getList ();
}
