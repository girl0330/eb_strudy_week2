package com.sb.sbweek3.board.free;

import com.sb.sbweek3.common.FileUtils;
import com.sb.sbweek3.dto.BoardInfoDTO;
import com.sb.sbweek3.dto.SearchDTO;
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
