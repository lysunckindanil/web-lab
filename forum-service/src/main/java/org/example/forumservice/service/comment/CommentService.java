package org.example.forumservice.service.comment;

import org.example.forumservice.dto.comment.CommentDto;
import org.example.forumservice.dto.comment.CreateCommentDto;
import org.example.forumservice.dto.comment.DeleteCommentDto;
import org.example.forumservice.dto.comment.GetCommentsByIssueDto;
import org.example.forumservice.service.issue.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentService {
    List<CommentDto> getByIssue(GetCommentsByIssueDto dto);

    @Transactional
    void create(CreateCommentDto dto);

    @Transactional
    void delete(DeleteCommentDto dto);

    @Transactional
    void deleteAllByIssue(Long issueId);

    @Autowired
    void setIssueService(@Lazy IssueService issueService);
}
