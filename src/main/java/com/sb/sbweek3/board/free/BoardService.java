package com.sb.sbweek3.board.free;

import com.sb.sbweek3.dto.BoardInfoDTO;
import com.sb.sbweek3.dto.PageInfoDTO;
import com.sb.sbweek3.dto.SearchDTO;

import java.util.List;
import java.util.Map;

public interface BoardService {
    List<BoardInfoDTO> getList();

    int getListTotal();

    int getSearchListTotal(String startDate, String endDate, int categoryId, String searchKeyword);

    List<SearchDTO> getListBySearch(String startDate, String endDate, int categoryId, String searchKeyword);

    Map<String, String> saveBoard(BoardInfoDTO boardInfoDTO);

    BoardInfoDTO getDetailByBoardId(int boardId);


    Map<String, String> updateBoard(BoardInfoDTO boardInfoDTO);

    List<BoardInfoDTO> getBoardList(int page);

    PageInfoDTO pagingParam(int page);
}
