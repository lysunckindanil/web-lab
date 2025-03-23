package org.example.webapp.service.comment;

import org.example.webapp.dto.forum.comment.CommentDto;
import org.example.webapp.dto.forum.comment.CreateCommentDto;
import org.example.webapp.dto.forum.comment.DeleteCommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> getByIssueId(Long issueId);

    void create(CreateCommentDto dto);

    void delete(DeleteCommentDto dto);
}
