package com.sb.sbweek3.board.free;

import com.sb.sbweek3.common.FileUtils;
import com.sb.sbweek3.dto.*;
import com.sb.sbweek3.exception.CustomException;
import com.sb.sbweek3.exception.ExceptionErrorCode;
import com.sb.sbweek3.file.FileMapper;
import com.sb.sbweek3.file.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{

    private final BoardMapper boardMapper;
//    private final CategoryMapper categoryMapper;
    private final FileUtils fileUtils;
    private final FileServiceImpl fileServiceImpl;
    private final FileMapper fileMapper;

    int pageLimit = 12;
    int blockLimit = 10;


    public List<BoardInfoDTO> getBoardList(int page) {
        /*
        보여지는 item 수 : 12개
        1 페이지 요청 : 0
        2 페이지를 요청 : 12
        3 페이지를 요청 : 24
         */

        int pagingStart = (page -1) * pageLimit;
        Map<String, Integer> pagingParams = new HashMap<>();
        pagingParams.put("start", pagingStart);
        pagingParams.put("limit", pageLimit);
        List<BoardInfoDTO> pagingList = boardMapper.pagingList(pagingParams);

        return pagingList;
    }

    public PageInfoDTO pagingParam(int page) {
        int boardTotal = boardMapper.getBoardDataTotal();
        int maxPage = (int) (Math.ceil((double) boardTotal / pageLimit));
        // 시자페이지 값 계산
        int startPage = (int)(Math.ceil((double) page / blockLimit) -1 )  * blockLimit + 1;
        int endPage = startPage + blockLimit -1 ;
        if (endPage == startPage) {
            endPage = maxPage;
        }

        return PageInfoDTO.builder()
                          .page(page)
                          .maxPage(maxPage)
                          .startPage(startPage)
                          .endPage(endPage)
                          .build();
    }
    public int getListTotal() {
        return boardMapper.getBoardDataTotal();
    }

    public List<BoardInfoDTO> getList() {
        return boardMapper.getList();
    }


    public List<SearchDTO> getListBySearch(String startDate, String endDate, int categoryId, String searchKeyword) {

        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setSearchKeyword(searchKeyword);
        searchDTO.setCategoryId(categoryId);
        searchDTO.setEndDate(endDate);
        searchDTO.setStartDate(startDate);

        System.out.println("검색 키워드들 확인 :; "+searchDTO);
        return boardMapper.getListBySearch(searchDTO);
    }

    public int getSearchListTotal(String startDate, String endDate, int categoryId, String searchKeyword) {
        return boardMapper.getSearchListTotal(startDate, endDate, categoryId, searchKeyword);
    }

    public Map<String, String> saveBoard(BoardInfoDTO boardInfoDTO) {
        try {
            Map<String, String> map = new HashMap<>();

            System.out.println("보드 확인 : "+boardInfoDTO);
            boardMapper.saveBoard(boardInfoDTO);
            int boardId = boardInfoDTO.getBoardId(); // 저장된 게시글 pk값

            /**파일 처리*/
            fileServiceImpl.saveFiles(boardId, boardInfoDTO.getFiles());
            map.put("code", "success");
            map.put("message", "게시글 작성 완료");
            return map;

        } catch (Exception e) {
            // 로그 프레임워크를 통해 예외 기록
            log.error("예기치 못하게 저장이 되지 않음 : " + e.getMessage(), e); //todo : 화면에 에러메시지를 노출시키려면?
            throw new CustomException(ExceptionErrorCode.GENERIC_ERROR);
        }
    }

    public BoardInfoDTO getDetailByBoardId(int boardId) {
        return boardMapper.getDetailByBoardId(boardId);
    }

    public boolean checkPassword(BoardInfoDTO boardInfoDTO) {
        int savedPassword = boardMapper.findPasswordByBoardId(boardInfoDTO.getBoardId());
        System.out.println("savedPassword ::: "+savedPassword);

        int inputPassword = boardInfoDTO.getPassword();
        if (inputPassword != savedPassword) {
            return false;
        }
        boardMapper.deleteBoard(boardInfoDTO.getBoardId());
        System.out.println(" 게시글 삭제 성공 ");
        return true;
    }

    public Map<String, String> updateBoard(BoardInfoDTO boardInfoDTO) {
        try {
            Map<String, String> map = new HashMap<>();

            System.out.println("보드 확인 : "+boardInfoDTO);
            boardMapper.updateBoard(boardInfoDTO);
            map.put("code", "success");
            map.put("message", "게시글 수정 완료");
            return map;
        } catch (Exception e) {
            // 로그 프레임워크를 통해 예외 기록
            log.error("예기치 못하게 저장이 되지 않음 : " + e.getMessage(), e); //todo : 화면에 에러메시지를 노출시키려면?
            throw new CustomException(ExceptionErrorCode.GENERIC_ERROR);
        }
    }

}
