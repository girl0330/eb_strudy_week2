package com.sb.sbweek3.comment;

import com.sb.sbweek3.dto.CommentInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    void saveComment(CommentInfoDTO commentInfoDTO);

    List<CommentInfoDTO> getCommentListByBoardId(int commentId);

    List<CommentInfoDTO> getCommentDetailByBoardId(int boardId);
}
