package com.sb.sbweek3.comment;

import com.sb.sbweek3.dto.CommentInfoDTO;
import com.sb.sbweek3.exception.CustomException;
import com.sb.sbweek3.exception.ExceptionErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment/save")
    @ResponseBody
    public List<CommentInfoDTO> saveComment(@ModelAttribute CommentInfoDTO commentInfoDTO) { //댓글 내용, 보드Id
        if (commentInfoDTO == null) throw new CustomException(ExceptionErrorCode.NULL_POINTER_EXCEPTION);

        commentService.saveComment(commentInfoDTO);

        List<CommentInfoDTO> commentList = commentService.getCommentListByBoardId(commentInfoDTO.getBoardId());
        return commentList;
    }

}
