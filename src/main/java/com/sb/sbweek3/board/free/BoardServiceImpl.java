package com.sb.sbweek3.board.free;

import com.sb.sbweek3.common.FileUtils;
import com.sb.sbweek3.dto.BoardInfoDTO;
import com.sb.sbweek3.dto.PageInfoDTO;
import com.sb.sbweek3.dto.SearchDTO;
import com.sb.sbweek3.exception.CustomException;
import com.sb.sbweek3.exception.ExceptionErrorCode;
import com.sb.sbweek3.file.FileMapper;
import com.sb.sbweek3.file.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{
    private static final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);
    private final BoardMapper boardMapper;
//    private final CategoryMapper categoryMapper;
    private final FileUtils fileUtils;
    private final FileServiceImpl fileServiceImpl;
    private final FileMapper fileMapper;

    int pageLimit = 12;
    int blockLimit = 10;


    public List<BoardInfoDTO> getBoardList(int page, SearchDTO searchDTO) {
        int pagingStart = (page -1) * pageLimit;
        searchDTO.setStart(pagingStart);
        searchDTO.setLimit(pageLimit);
        List<BoardInfoDTO> pagingList = boardMapper.getBoardList(searchDTO);
        return pagingList;
    }

    public PageInfoDTO pageButton(int page, SearchDTO searchDTO) {
        System.out.println("1");
        int boardTotal = getListTotal(searchDTO);
        logger.debug("Total boards: {}", boardTotal);
        System.out.println("3");
        int maxPage = (int) Math.ceil((double) boardTotal / pageLimit);

        int startPage = (int) (Math.ceil((double) page / blockLimit) - 1) * blockLimit + 1;
        int endPage = startPage + blockLimit - 1;

        if (endPage > maxPage) {
            endPage = maxPage;
        }

        return PageInfoDTO.builder()
                .page(page)
                .maxPage(maxPage)
                .startPage(startPage)
                .endPage(endPage)
                .build();
    }

    public int getListTotal(SearchDTO searchDTO) {
        System.out.println("2");
        return boardMapper.getListTotal(searchDTO);
    }

    public int saveBoard(BoardInfoDTO boardInfoDTO) {
        boardMapper.saveBoard(boardInfoDTO);
        int boardId = boardInfoDTO.getBoardId(); // 저장된 게시글 pk값
        return boardId;
    }
    public BoardInfoDTO getDetailByBoardId(int boardId)  {
        if (!checkBoardId(boardId)) {
            throw new CustomException(ExceptionErrorCode.INVALID_BOARD_ID);
        }

        BoardInfoDTO boardInfo = boardMapper.getDetailByBoardId(boardId);
        if (boardInfo == null) {
            throw new CustomException(ExceptionErrorCode.BOARD_NOT_FOUND);
        }
        return boardInfo;
    }

    public void checkPassword(BoardInfoDTO boardInfoDTO) {
        if (!checkBoardId(boardInfoDTO.getBoardId())) {
            throw new CustomException(ExceptionErrorCode.INVALID_BOARD_ID);
        }
        int savedPassword = boardMapper.findPasswordByBoardId(boardInfoDTO.getBoardId());

        int inputPassword = boardInfoDTO.getPassword();
        if (inputPassword != savedPassword) {
            throw new CustomException(ExceptionErrorCode.PASSWORD_INCORRECT_TOKEN);
        }

        boardMapper.deleteBoard(boardInfoDTO.getBoardId());
    }

    /**
     * 게시글 수정
     * @param boardInfoDTO
     * @return
     */
    public void updateBoard(BoardInfoDTO boardInfoDTO) {
        if (boardInfoDTO == null) throw new CustomException(ExceptionErrorCode.NULL_POINTER_EXCEPTION);
        boardMapper.updateBoard(boardInfoDTO);
    }

    /**
     * 조회수 증가
     * @param boardId - BoardId로 조회
     */
    public void setView(int boardId) {
        boardMapper.setView(boardId);
    }

    public boolean checkBoardId(int boardId) {
        return boardMapper.check(boardId);
    }

}
