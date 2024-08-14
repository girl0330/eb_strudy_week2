package com.sb.sbweek3.board.free;

import com.sb.sbweek3.dto.BoardInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardMapper boardMapper;

    public List<BoardInfoDTO> getList() {
        System.out.println("보드 리스트 서비스 ^^");
        return boardMapper.getList();
    }

    public int saveBoard(BoardInfoDTO boardInfoDTO) {
        boardMapper.saveBoard(boardInfoDTO);
        return boardInfoDTO.getBoardId();

    }
}
