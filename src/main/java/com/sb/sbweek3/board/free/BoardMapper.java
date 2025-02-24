package com.sb.sbweek3.board.free;

import com.sb.sbweek3.dto.BoardInfoDTO;
import com.sb.sbweek3.dto.SearchDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

//mybatisX 플러그인
@Mapper
public interface BoardMapper {
    List<BoardInfoDTO> getBoardList (SearchDTO searchDTO);

    int getListTotal(SearchDTO searchDTO);

    int saveBoard(BoardInfoDTO boardInfoDTO);

    BoardInfoDTO getDetailByBoardId(int boardId);

    void updateBoard(BoardInfoDTO boardInfoDTO);

    int findPasswordByBoardId(int boardId);

    void deleteBoard(int boardId);

    void setView(int boardId);

    boolean check(int boardId);
}
