package org.example.webapp.client;

import org.example.webapp.dto.forum.comment.CommentDto;
import org.example.webapp.dto.forum.comment.CreateCommentApiDto;
import org.example.webapp.dto.forum.comment.DeleteCommentApiDto;
import org.example.webapp.dto.forum.comment.GetCommentsByIssueApiDto;

import java.util.List;

public interface CommentRestClient {
    List<CommentDto> getByIssue(GetCommentsByIssueApiDto dto);

    void create(CreateCommentApiDto dto);

    void delete(DeleteCommentApiDto dto);
}
