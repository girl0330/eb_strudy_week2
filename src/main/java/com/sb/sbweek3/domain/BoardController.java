package com.sb.sbweek3.domain;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//todo : @GetMapping, spring boot version 2.7.x
@Controller
public class BoardController {
    @GetMapping("/list")
    public String showList() {
        System.out.println("리스트 ㅅㅂ");
        return "board/list";
    }
}
