package com.sb.sbweek3.board.free;

import com.sb.sbweek3.common.FileUtils;
import com.sb.sbweek3.dto.BoardInfoDTO;
import com.sb.sbweek3.dto.FileInfoDTO;
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
    private final FileServiceImpl fileService;
    private final FileMapper fileMapper;

    public int getListTotal() {
        return boardMapper.getBoardDataTotal();
    }

    public List<BoardInfoDTO> getList() {
        return boardMapper.getList();
    }

    public Map<String, Object> getData() {
        Map<String, Object> map = new HashMap<>();
        List<BoardInfoDTO> boardData = boardMapper.getList();
        int boardDataTotal = boardMapper.getBoardDataTotal();

        map.put("boardData", boardData);
        map.put("boardDataTotal", boardDataTotal);
        return map;
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
    public Map<String, String> saveBoard(BoardInfoDTO boardInfoDTO) {
        Map<String, String> map = new HashMap<>();
        try {
            boardMapper.saveBoard(boardInfoDTO);
            int boardId = boardInfoDTO.getBoardId();
            System.out.println("보드 피케이 확인 : "+boardId);

            /**파일 처리*/
            List<FileInfoDTO> files = fileUtils.uploadFiles(boardInfoDTO.getFiles());
            fileService.saveFiles(boardId, files);

            //todo: 정한 후 massage화면에 안나오고 있음. 
            map.put("code", "success");
            map.put("message", "게시글 작성 성공!");
            System.out.println("map 확인 : "+map);
            return map;
        } catch (Exception e) {
            // 로그 프레임워크를 통해 예외 기록
            log.error("예기치 못하게 저장이 되지 않음 : " + e.getMessage(), e); //todo : 화면에 에러메시지를 노출시키려면?
            throw new CustomException(ExceptionErrorCode.GENERIC_ERROR);
        }
    }

    public Map<String, Object> getDetailById(int boardId) {
        Map<String, Object> map = new HashMap<>();
        BoardInfoDTO boardDetail = boardMapper.findById(boardId);
        System.out.println("게시글 상세보기 : "+ boardDetail);
        map.put("detail", boardDetail);

        List<FileInfoDTO> files = fileService.findFileIdByBoardId(boardId);
        System.out.println("파일들 확인 : "+files);
        map.put("files", files);
        return map;
    }
}
