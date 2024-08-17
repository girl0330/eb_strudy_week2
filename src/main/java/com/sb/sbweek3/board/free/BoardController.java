package com.sb.sbweek3.board.free;

import com.sb.sbweek3.category.CategoryServiceImpl;
import com.sb.sbweek3.dto.BoardInfoDTO;
import com.sb.sbweek3.dto.CategoryInfoDTO;
import com.sb.sbweek3.dto.SearchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

//todo : @GetMapping, spring boot version 2.7.x
//todo : list부터 먼저 쭉 만들고 넘어  가기
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CategoryServiceImpl categoryService;

    @GetMapping("/board-list")
    public String showList(Model model) {
        List<CategoryInfoDTO> categoryList = categoryService.getCategoryList();

        Map<String, Object> map = boardService.getData();

//        List<BoardInfoDTO> boardInfoList = boardService.getList();
//        int total = boardService.getListTotal();

        System.out.println("boardData 확인 :: "+map.get("boardData"));
        System.out.println("boardDataTotal 확인 :: "+map.get("boardDataTotal"));

        model.addAttribute("lists", map.get("boardData"));
        model.addAttribute("total", map.get("boardDataTotal"));
        model.addAttribute("categoryLists",categoryList);
        return "board/list";
    }

    @ResponseBody
    @GetMapping("/ajax/board-list.do")
    public List<SearchDTO> getBoardList(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "categoryId", required = false) int categoryId,
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
            Model model) {

        List<SearchDTO> searchLists = boardService.getListBySearch(startDate, endDate, categoryId, searchKeyword);
        System.out.println("확인 :::  "+searchLists);

        int total = boardService.getListTotal();
        model.addAttribute("total", total);

        return searchLists;
    }

    @GetMapping("/board-post-page")
    public String postPage() {
        return "board/post";
    }

    @PostMapping("/ajax/board-save.do") //todo : ResponseEntity
    public Map<String, String> ajaxSaveBoard(BoardInfoDTO boardInfoDTO) {
        return boardService.saveBoard(boardInfoDTO);
    }

    //todo : @PathVariable과 @RequestParam(쿼리 스트링으로 받음 -> 가변적일 때 )의 차이점

    @GetMapping("/board")
    public String postDetail() {
        return "board/detail";
    }

    @GetMapping("/board-detail-page")
    public String detailPage(@RequestParam("boardId") int boardId, Model model) {
        Map<String, Object> map = boardService.getDetailById(boardId);
        System.out.println("디테일 화면 확인 : "+map.get("detail"));
        model.addAttribute("detail", map.get("detail"));
        model.addAttribute("files", map.get("files"));
        return "board/detail";
    }
    @GetMapping("/board-update-page")
    public String updatePage() {
        return null;
    }

    @PostMapping("/ajax/board-update")
    public String ajaxBoardUpdate() {
        return null;
    }

}
