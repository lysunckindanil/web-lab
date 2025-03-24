package org.example.webapp.service.comment;

import lombok.RequiredArgsConstructor;
import org.example.webapp.client.CommentRestClient;
import org.example.webapp.dto.forum.comment.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRestClient commentRestClient;

    @Override
    public List<CommentDto> getByIssueId(Long issueId) {
        GetCommentsByIssueApiDto request = GetCommentsByIssueApiDto
                .builder()
                .username(SecurityContextHolder.getContext().getAuthentication().getName())
                .issueId(issueId).build();
        return commentRestClient.getByIssue(request);
    }

    @Override
    public void create(CreateCommentDto dto) {
        CreateCommentApiDto request = CreateCommentApiDto.builder()
                .content(dto.getContent())
                .issueId(dto.getIssueId())
                .authorUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .build();
        commentRestClient.create(request);
    }

    @Override
    public void delete(DeleteCommentDto dto) {
        DeleteCommentApiDto request = DeleteCommentApiDto.builder()
                .commentId(dto.getCommentId())
                .username(SecurityContextHolder.getContext().getAuthentication().getName())
                .build();
        commentRestClient.delete(request);
    }
}
