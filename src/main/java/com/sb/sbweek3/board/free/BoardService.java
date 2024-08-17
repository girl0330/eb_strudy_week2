package com.sb.sbweek3.board.free;

import com.sb.sbweek3.dto.BoardInfoDTO;
import com.sb.sbweek3.dto.SearchDTO;

import java.util.List;
import java.util.Map;

public interface BoardService {
    List<BoardInfoDTO> getList();

    List<SearchDTO> getListBySearch(String startDate, String endDate, int categoryId, String searchKeyword);

    Map<String, String> saveBoard(BoardInfoDTO boardInfoDTO);

    Map<String, Object> getDetailById(int boardId);

    Map<String, Object> getData();

    int getListTotal();
}
