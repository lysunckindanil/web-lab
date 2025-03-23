package org.example.webapp.service.comment;

import lombok.RequiredArgsConstructor;
import org.example.webapp.client.CommentRestClient;
import org.example.webapp.dto.forum.comment.*;
import org.example.webapp.model.User;
import org.example.webapp.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRestClient commentRestClient;
    private final UserService userService;

    @Override
    public List<CommentDto> getByIssueId(Long issueId) {
        GetCommentsByIssueApiDto request = GetCommentsByIssueApiDto.builder()
                .issueId(issueId).build();
        return validateAll(commentRestClient.getByIssue(request));
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

    private List<CommentDto> validateAll(List<CommentDto> comments) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.loadUserByUsername(username);
        if (user == null) return null;

        if (user.hasRole("ROLE_REDACTOR")) {
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
