package com.sb.sbweek3.board.free;

import com.sb.sbweek3.dto.BoardInfoDTO;
import com.sb.sbweek3.dto.PageInfoDTO;
import com.sb.sbweek3.dto.SearchDTO;

import java.util.List;
import java.util.Map;

public interface BoardService {
    List<BoardInfoDTO> getBoardList(int page, SearchDTO searchDTO);

    PageInfoDTO pagingParam(int page, SearchDTO searchDTO);

    int getListTotal(SearchDTO searchDTO);

//    List<BoardInfoDTO> getList();

//    int getSearchListTotal(String startDate, String endDate, int categoryId, String searchKeyword);
//

//    List<SearchDTO> getListBySearch(String startDate, String endDate, int categoryId, String searchKeyword);

    Map<String, String> saveBoard(BoardInfoDTO boardInfoDTO);

    BoardInfoDTO getDetailByBoardId(int boardId);


    Map<String, String> updateBoard(BoardInfoDTO boardInfoDTO);

    boolean checkPassword(BoardInfoDTO boardInfoDTO);

    /* 조회수 처리 */
    void setView(int boardId);
}
