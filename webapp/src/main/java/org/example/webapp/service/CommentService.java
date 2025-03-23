package org.example.webapp.service;

import lombok.RequiredArgsConstructor;
import org.example.webapp.client.CommentRestClient;
import org.example.webapp.dto.forum.comment.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRestClient commentRestClient;

    public List<CommentDto> getByIssueId(Long issueId) {
        GetCommentsByIssueApiDto request = GetCommentsByIssueApiDto.builder()
                .issueId(issueId).build();
        return commentRestClient.getByIssue(request);
    }

    public void create(CreateCommentDto dto) {
        CreateCommentApiDto request = CreateCommentApiDto.builder()
                .content(dto.getContent())
                .issueId(dto.getIssueId())
                .authorUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .build();
        commentRestClient.create(request);
    }

    public void delete(DeleteCommentDto dto) {
        DeleteCommentApiDto request = DeleteCommentApiDto.builder()
                .commentId(dto.getCommentId())
                .username(SecurityContextHolder.getContext().getAuthentication().getName())
                .build();
        commentRestClient.delete(request);
    }
}
