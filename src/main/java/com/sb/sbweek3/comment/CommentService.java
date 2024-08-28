package com.sb.sbweek3.comment;

import com.sb.sbweek3.dto.CommentInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentMapper commentMapper;
    public void saveComment(CommentInfoDTO commentInfoDTO) {
        commentMapper.saveComment(commentInfoDTO);
    }

    public List<CommentInfoDTO> getCommentListByBoardId(int commentId) {
        return commentMapper.getCommentListByBoardId(commentId);
    }

    public List<CommentInfoDTO> getDetailByBoardId(int boardId) {
        System.out.println("댓글 리스트 확인 : "+ commentMapper.getDetailByBoardId(boardId));
        return commentMapper.getDetailByBoardId(boardId);
    }
}
