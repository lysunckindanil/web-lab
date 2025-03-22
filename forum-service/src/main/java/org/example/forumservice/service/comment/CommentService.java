package org.example.forumservice.service.comment;

import org.example.forumservice.dto.comment.CreateCommentDto;
import org.example.forumservice.dto.comment.DeleteCommentDto;
import org.example.forumservice.dto.comment.GetCommentsByIssueDto;
import org.example.forumservice.model.Comment;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentService {
    List<Comment> getByIssue(GetCommentsByIssueDto dto);

    @Transactional
    void create(CreateCommentDto dto);

    @Transactional
    void delete(DeleteCommentDto dto);

    @Transactional
    void deleteAllByIssue(Long issueId);
}
