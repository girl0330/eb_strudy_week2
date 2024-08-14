package com.sb.sbweek3.board.free;

import com.sb.sbweek3.common.FileUtils;
import com.sb.sbweek3.dto.BoardInfoDTO;
import com.sb.sbweek3.dto.FileInfoDTO;
import com.sb.sbweek3.file.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

//todo : @GetMapping, spring boot version 2.7.x
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final FileUtils fileUtils;
    private final FileServiceImpl fileService;
    //todo : 환면에 보일 부분은 snakeCase
    @GetMapping("/board-list")
    public String showList(Model model) {
        System.out.println("리스트 등장 ^^");
        List<BoardInfoDTO> boardInfoList = boardService.getList();

        System.out.println("보드 리스트들 확인 : "+boardInfoList);
        model.addAttribute("lists", boardInfoList);
        return "board/list";
    }

    @GetMapping("/board-post-page")
    public String postPage() {
        System.out.println("글작성 페이지 ^^");
        return "board/post";
    }

    @PostMapping("/ajax/board-save.do")
    public String ajaxSaveBoard(BoardInfoDTO boardInfoDTO, Model model) {
        System.out.println("ajax로 넘어옴?"+boardInfoDTO);
        int boardId = boardService.saveBoard(boardInfoDTO);
        System.out.println("보드아이디 확인 :: "+boardId);
        List<FileInfoDTO> files = fileUtils.uploadFiles(boardInfoDTO.getFiles());
        System.out.println("file확인 "+files);
        fileService.saveFiles(boardId,files);
        System.out.println("파일 저장??");
        return null;
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
