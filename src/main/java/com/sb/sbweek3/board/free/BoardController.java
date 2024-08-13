package com.sb.sbweek3.board.free;

import com.sb.sbweek3.dto.BoardInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

//todo : @GetMapping, spring boot version 2.7.x
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    @GetMapping("/list")
    public String showList(Model model) {
        System.out.println("리스트 등장 ^^");
        List<BoardInfoDTO> boardInfoList = boardService.getList();

        System.out.println("보드 리스트들 확인 : "+boardInfoList);
        model.addAttribute("lists", boardInfoList);
        return "board/list";
    }

    @GetMapping("/post")
    public String postPage() {
        System.out.println("글작성 페이지 ^^");
        return "board/post";
    }

}
