package com.sb.sbweek3.board.free;

import com.sb.sbweek3.dto.BoardInfoDTO;
import com.sb.sbweek3.dto.SearchDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
//mybatisX 플러그인
@Mapper
public interface BoardMapper {
    List<BoardInfoDTO> getList ();

    List<SearchDTO> getListBySearch(SearchDTO searchDTO);

    int saveBoard(BoardInfoDTO boardInfoDTO);

    BoardInfoDTO findById(int boardId);

    int getBoardDataTotal();
}
