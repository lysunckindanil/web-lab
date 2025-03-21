package org.example.forumservice.service;

import org.example.forumservice.dto.comment.CreateCommentDto;
import org.example.forumservice.dto.comment.DeleteCommentDto;
import org.example.forumservice.dto.comment.GetByIssueDto;
import org.example.forumservice.entity.Comment;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentService {
    List<Comment> getByIssue(GetByIssueDto dto);

    @Transactional
    void create(CreateCommentDto dto);

    @Transactional
    void delete(DeleteCommentDto dto);

    @Transactional
    void deleteAllByIssue(Long issueId);
}
