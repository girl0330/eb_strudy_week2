package com.sb.sbweek3.board.free;

import com.sb.sbweek3.dto.BoardInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardInfoDTO> getList ();
}
