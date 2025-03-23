package org.example.webapp.service;

import lombok.RequiredArgsConstructor;
import org.example.webapp.client.CommentRestClient;
import org.example.webapp.dto.forum.comment.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRestClient commentRestClient;
    private final UserDetailsService userDetailsService;

    public List<CommentDto> getByIssueId(Long issueId) {
        GetCommentsByIssueApiDto request = GetCommentsByIssueApiDto.builder()
                .issueId(issueId).build();
        return validateAll(commentRestClient.getByIssue(request));
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

    private List<CommentDto> validateAll(List<CommentDto> comments) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userDetailsService.loadUserByUsername(username)
                .getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_REDACTOR"))) {
            for (CommentDto comment : comments) {
                comment.setCanDelete(true);
            }
            return comments;
        }

        for (CommentDto comment : comments) {
            if (comment.getAuthorUsername().equals(username)) {
                comment.setCanDelete(true);
            }
        }
        return comments;
    }
}
