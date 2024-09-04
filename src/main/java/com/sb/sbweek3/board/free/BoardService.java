package com.sb.sbweek3.board.free;

import com.sb.sbweek3.dto.BoardInfoDTO;
import com.sb.sbweek3.dto.PageInfoDTO;
import com.sb.sbweek3.dto.SearchDTO;

import java.util.List;

public interface BoardService {
    List<BoardInfoDTO> getBoardList(int page, SearchDTO searchDTO);

    PageInfoDTO pageButton(int page, SearchDTO searchDTO);

    int getListTotal(SearchDTO searchDTO);

    int saveBoard(BoardInfoDTO boardInfoDTO);

    BoardInfoDTO getDetailByBoardId(int boardId);

    void updateBoard(BoardInfoDTO boardInfoDTO);

    void checkPassword(BoardInfoDTO boardInfoDTO);

    /* 조회수 처리 */
    void setView(int boardId);
}
