package com.sb.sbweek3.board.free;


import com.sb.sbweek3.category.CategoryServiceImpl;
import com.sb.sbweek3.comment.CommentService;
import com.sb.sbweek3.common.FileUtils;
import com.sb.sbweek3.dto.*;
import com.sb.sbweek3.exception.CustomException;
import com.sb.sbweek3.exception.ExceptionErrorCode;
import com.sb.sbweek3.file.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

//todo : @GetMapping, spring boot version 2.7.x
//todo : list부터 먼저 쭉 만들고 넘어  가기
@Controller
@RequiredArgsConstructor
public class BoardController {
    private static final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);
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
        List<CategoryInfoDTO> categoryList = categoryServiceImpl.getCategoryList();

        int total = boardService.getListTotal(searchDTO);
        List<BoardInfoDTO> boardList = boardService.getBoardList(page, searchDTO);
        PageInfoDTO pageInfoDTO = boardService.pageButton(page, searchDTO);

        model.addAttribute("list", boardList);
        model.addAttribute("total", total);
        model.addAttribute("paging", pageInfoDTO);
        model.addAttribute("category", categoryList);
        model.addAttribute("search", searchDTO);

        return "board/list";
    }

    /** 글 작성 */
    @GetMapping("/board-post-page")
    public String postPage(Model model) {
        List<CategoryInfoDTO> categoryList = categoryServiceImpl.getCategoryList();
        model.addAttribute("category", categoryList);
        return "board/post";
    }

    /**
     * @param boardInfoDTO : 입력받은 데이터
     */
    @ResponseBody
    @PostMapping("/ajax/board-save.do")
    public ResponseEntity<?> ajaxSaveBoard(BoardInfoDTO boardInfoDTO) {
        if (boardInfoDTO == null) throw new CustomException(ExceptionErrorCode.NULL_POINTER_EXCEPTION);
        int boardId = boardService.saveBoard(boardInfoDTO);

        if (boardInfoDTO.getFiles() != null && !boardInfoDTO.getFiles().isEmpty()) {
            fileServiceImpl.saveFiles(boardId, boardInfoDTO.getFiles());
        }

        SuccessResponseDTO successResponse = SuccessResponseDTO.builder()
                .statusCode(200)
                .message("게시글이 성공적으로 등록되었습니다.")
                .redirectUrl("board-detail-page?boardId=" + boardId+ "&viewSet=yes")
                .build();
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    //todo : @PathVariable과 @RequestParam(쿼리 스트링으로 받음 -> 가변적일 때 )의 차이점

    /**
     * 상세보기
     * @param boardId : 게시글 pk
     * @param viewSet : 조회수
     * @return
     */
    @GetMapping("/board-detail-page")
    public String detailPage(@RequestParam("boardId") int boardId,
                             @RequestParam(value = "viewSet", defaultValue = "no") String viewSet, Model model) {

        if ("yes".equals(viewSet)) {
            boardService.setView(boardId);
            return "redirect:board-detail-page?boardId=" + boardId;
        }

        try {
            BoardInfoDTO boardDetail = boardService.getDetailByBoardId(boardId);
            List<CommentInfoDTO> commentDetail = commentService.getCommentDetailByBoardId(boardId);

            model.addAttribute("detail", boardDetail);
            model.addAttribute("comment", commentDetail);
            return "board/detail";
        }
        catch (CustomException e) {
            if (e.getExceptionErrorCode() == ExceptionErrorCode.BOARD_NOT_FOUND) {
                return "error/404";
            }
            throw e;
        }
    }

    /**
     * 비밀번호 일치 확인
     * @param boardId 게시글 pk
     * @return 체크하는 뷰
     */
    @GetMapping("/board-delete-check")
    public String checkPassword(@RequestParam("boardId") int boardId, Model model) {
        model.addAttribute("boardId", boardId);
        return "board/password-check";
    }

    /**
     * 비밀번호 확인 후 삭제
     * @param boardInfoDTO
     * @return
     */
    @PostMapping("/ajax/delete-check.do")
    public ResponseEntity<?> checkPassword(BoardInfoDTO boardInfoDTO) {
        boardService.checkPassword(boardInfoDTO);

        SuccessResponseDTO successResponse = SuccessResponseDTO.builder()
                .statusCode(200)
                .message("게시글이 성공적으로 삭제되었습니다.")
                .redirectUrl("/board-list")
                .build();
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    /**
     *
     * @param boardId
     * @param page
     * @param model
     * @return
     */
    @GetMapping("/board-update-page")
    public String updatePage(@RequestParam("boardId") int boardId, @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
        BoardInfoDTO boardDetail = boardService.getDetailByBoardId(boardId);

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
            BoardInfoDTO boardInfoDTO) throws IOException {

        //새로운 파일 저장
        if (files != null && !files.isEmpty()) {
            fileServiceImpl.saveFiles(boardId, files);
        }

        //기존 파일 삭제
        if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
            List<FileInfoDTO> deleteFiles = fileServiceImpl.findFileByFileIds(deleteFileIds); // 삭제할 파일 조회
            fileUtils.deleteFiles(deleteFiles); // 파일 삭제 (from disk)
            fileServiceImpl.deleteFileByFileIds(deleteFileIds);  // 파일 삭제 (from database)
        }

        //게시글 내용 수정
        boardService.updateBoard(boardInfoDTO);
        SuccessResponseDTO successResponse = SuccessResponseDTO.builder()
                .statusCode(200)
                .message("게시글이 성공적으로 수정되었습니다.")
                .redirectUrl("board-detail-page?boardId=" + boardInfoDTO.getBoardId() + "&viewSet=yes")
                .build();
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }
}
