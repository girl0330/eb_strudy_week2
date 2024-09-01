package com.sb.sbweek3.board.free;


import com.sb.sbweek3.category.CategoryServiceImpl;
import com.sb.sbweek3.comment.CommentService;
import com.sb.sbweek3.common.FileUtils;
import com.sb.sbweek3.dto.*;
import com.sb.sbweek3.file.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 게시글 목록
     * @param page - 페이지 블럭 값
     * @return - 페이지 화면
     */
    @GetMapping("/board-list")
    public String showPagingList(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") int page, @ModelAttribute SearchDTO searchDTO) {
        System.out.println("page = " + page);
        System.out.println("searchDTO 확인 : "+searchDTO);
        List<CategoryInfoDTO> categoryList = categoryServiceImpl.getCategoryList(); //검색
        System.out.println("categoryList 확인 : "+categoryList);
        List<BoardInfoDTO> boardList = boardService.getBoardList(page, searchDTO);
        System.out.println("확인 : "+ searchDTO);
        PageInfoDTO pageInfoDTO = boardService.pagingParam(page, searchDTO); //
        int total = boardService.getListTotal(searchDTO);

        System.out.println("boardList 확인 : "+boardList);

        model.addAttribute("list", boardList);
        model.addAttribute("total", total);
        model.addAttribute("paging", pageInfoDTO);
        model.addAttribute("category",categoryList);
        model.addAttribute("search",searchDTO);
        return "board/list";
    }
//
//    //todo : 동적 리스트 total
//    @GetMapping("/ajax/search/board-list.do")
//    public ResponseEntity<Map<String, Object>> getSearchBoardList(
//            @RequestParam(value = "startDate", required = false) String startDate,
//            @RequestParam(value = "endDate", required = false) String endDate,
//            @RequestParam(value = "categoryId", required = false) Integer categoryId,  // int에서 Integer로 변경
//            @RequestParam(value = "searchKeyword", required = false) String searchKeyword) {
//
//        System.out.println("startDate = " + startDate + ", endDate = " + endDate + ", categoryId = " + categoryId + ", searchKeyword = " + searchKeyword);
//        List<SearchDTO> searchLists = boardService.getListBySearch(startDate, endDate, categoryId, searchKeyword);
//        int searchListsTotal = boardService.getSearchListTotal(startDate, endDate, categoryId, searchKeyword);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("searchLists", searchLists);
//        response.put("searchListsTotal", searchListsTotal);
////        model.addAttribute("total", total);
////        model.addAttribute("searchLists", searchLists);
//
//        return ResponseEntity.ok(response);
//    }

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
    public String detailPage(@RequestParam("boardId") int boardId,
                             @RequestParam(value = "viewSet", defaultValue = "no") String viewSet, Model model) {

        if ("yes".equals(viewSet)) {
            boardService.setView(boardId);
            return "redirect:board-detail-page?boardId=" + boardId;
        }

        BoardInfoDTO boardDetail = boardService.getDetailByBoardId(boardId);
        List<CommentInfoDTO> commentDetail = commentService.getDetailByBoardId(boardId);

        model.addAttribute("detail", boardDetail);
        model.addAttribute("comment", commentDetail);
        return "board/detail"; //todo : ResponseEntity
    }

    @GetMapping("/board-delete-check")
    public String checkPassword(@RequestParam("boardId") int boardId, Model model) {
        model.addAttribute("boardId", boardId);
        return "board/password-check";
    }

    @PostMapping("/ajax/delete-check.do")
    public ResponseEntity<?> checkPassword(BoardInfoDTO boardInfoDTO) {
        System.out.println("boardInfoDTO = " + boardInfoDTO);
        boolean result = boardService.checkPassword(boardInfoDTO);

        if (result) {
            SuccessResponseDTO successResponse = SuccessResponseDTO.builder()
                    .statusCode(200)
                    .message("게시글이 성공적으로 삭제되었습니다.")
                    .redirectUrl("/board-list")
                    .build();
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } else {
            ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                    .statusCode(401)
                    .message("비밀번호가 일치하지 않습니다.")
                    .errorDetails("입력하신 비밀번호가 일치하지 않습니다. 다시 시도해 주세요.")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
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
