package com.sb.sbweek3.board.free;


import com.sb.sbweek3.category.CategoryServiceImpl;
import com.sb.sbweek3.comment.CommentService;
import com.sb.sbweek3.common.FileUtils;
import com.sb.sbweek3.dto.*;
import com.sb.sbweek3.file.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//todo : @GetMapping, spring boot version 2.7.x
//todo : list부터 먼저 쭉 만들고 넘어  가기
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final CategoryServiceImpl categoryServiceImpl;
    private final FileServiceImpl fileServiceImpl;
    private final FileUtils fileUtils;

    @GetMapping("/board/paging")
    public String showPagingList(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        System.out.println("page = " + page);
        List<CategoryInfoDTO> categoryList = categoryServiceImpl.getCategoryList();
        List<BoardInfoDTO> boardList = boardService.getBoardList(page);
        PageInfoDTO pageInfoDTO = boardService.pagingParam(page);
        int total = boardService.getListTotal();

        model.addAttribute("list", boardList);
        model.addAttribute("total", total);
        model.addAttribute("paging", pageInfoDTO);
        model.addAttribute("category",categoryList);
        return "board/list";
    }

    //todo : 동적 리스트 total
    @ResponseBody
    @GetMapping("/ajax/search/board-list.do") //todo: (ajax)페이지 이동을 시켜줌 returnView
    public ResponseEntity<Map<String, Object>> getSearchBoardList(  //todo: dto로 만들어 사용
                                                                    @RequestParam(value = "startDate", required = false) String startDate,
                                                                    @RequestParam(value = "endDate", required = false) String endDate,
                                                                    @RequestParam(value = "categoryId", required = false) int categoryId,
                                                                    @RequestParam(value = "searchKeyword", required = false) String searchKeyword) {

        List<SearchDTO> searchLists = boardService.getListBySearch(startDate, endDate, categoryId, searchKeyword);
        int searchListsTotal = boardService.getSearchListTotal(startDate, endDate, categoryId, searchKeyword);

        Map<String, Object> response = new HashMap<>();
        response.put("searchLists", searchLists);
        response.put("searchListsTotal", searchListsTotal);
//        model.addAttribute("total", total);
//        model.addAttribute("searchLists", searchLists);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/board-post-page")
    public String postPage() {
        return "board/post";
    }

    @ResponseBody
    @PostMapping("/ajax/board-save.do")
    public Map<String, String> ajaxSaveBoard(BoardInfoDTO boardInfoDTO) {
        System.out.println("보드 확인 : "+boardInfoDTO);
        return boardService.saveBoard(boardInfoDTO);
    }

    //todo : @PathVariable과 @RequestParam(쿼리 스트링으로 받음 -> 가변적일 때 )의 차이점

    @GetMapping("/board-detail-page")
    public String detailPage(@RequestParam("boardId") int boardId, Model model) {
        BoardInfoDTO boardDetail = boardService.getDetailByBoardId(boardId);
        List<CommentInfoDTO> commentDetail = commentService.getDetailByBoardId(boardId);
        System.out.println("commentService 확인 : "+commentDetail);
        model.addAttribute("detail", boardDetail);
        model.addAttribute("comment", commentDetail);
        return "board/detail"; //todo : ResponseEntity
    }
    @GetMapping("/board-update-page")
    public String updatePage(@RequestParam("boardId") int boardId, @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
        BoardInfoDTO boardDetail = boardService.getDetailByBoardId(boardId);
        System.out.println("boardDetail     : " +boardDetail );

        List<FileInfoDTO> files = fileServiceImpl.findFileIdByBoardId(boardId);
        System.out.println("files      : "+files);
        model.addAttribute("detail", boardDetail);
        model.addAttribute("fileInfo",files);
        return "board/update";
    }

    @PostMapping("/ajax/{boardId}/board-update.do")
    @ResponseBody
    public ResponseEntity<?> ajaxUpdateBoard(
            @PathVariable int boardId,
            @RequestParam(value = "deleteFileIds[]", required = false) List<Integer> deleteFileIds,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            BoardInfoDTO boardInfoDTO) {
        System.out.println("엡데이트?");
        System.out.println("업뎃할 보드 아이디" + boardId);
        System.out.println("삭제할 파일 이이디 :    "+deleteFileIds);
        System.out.println("새로 등록할 파일 이이디 :    "+files);
        System.out.println("수정된 보드 내용 : "+ boardInfoDTO);

        //새로운 파일 저장
        if (files != null && !files.isEmpty()) {
            fileServiceImpl.saveFiles(boardId, files);
        }

        if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
            // 삭제할 파일 조회
            List<FileInfoDTO> deleteFiles = fileServiceImpl.findAllFileByIds(deleteFileIds);
            // 파일 삭제 (from disk)
            fileUtils.deleteFiles(deleteFiles);
            // 파일 삭제 (from database)
            fileServiceImpl.deleteAllFileByIds(deleteFileIds);
        }
        //게시글 내용 수정 todo: 부분적으로 수정하고 있는데 (게시글 제목, 내용만) 수정하지 못하게한 부분도 전부 받아와서 update해줘야 하나?
        Map<String, String> response = boardService.updateBoard(boardInfoDTO);

        return ResponseEntity.ok(response);
    }

}
