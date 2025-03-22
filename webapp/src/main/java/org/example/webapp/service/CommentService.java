package org.example.webapp.service;

import lombok.RequiredArgsConstructor;
import org.example.webapp.dto.forum.comment.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    public List<CommentDto> getByIssueId(Long issueId) {
        GetCommentsByIssueApiDto request = GetCommentsByIssueApiDto.builder()
                .issueId(issueId).build();

        CommentDto commentDto = new CommentDto(1L, "content1", LocalDateTime.now(), "user1");
        CommentDto commentDto1 = new CommentDto(2L, "content2", LocalDateTime.now(), "user2");
        CommentDto commentDto2 = new CommentDto(3L, "content3", LocalDateTime.now(), "user3");
        CommentDto commentDto3 = new CommentDto(4L, "content4", LocalDateTime.now(), "user4");
        return List.of(commentDto, commentDto1, commentDto2, commentDto3);
    }

    public void create(CreateCommentDto dto) {
        CreateCommentApiDto request = CreateCommentApiDto.builder()
                .content(dto.getContent())
                .issueId(dto.getIssueId())
                .authorUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .build();
    }

    public void delete(DeleteCommentDto dto) {
        DeleteCommentApiDto request = DeleteCommentApiDto.builder()
                .commentId(dto.getCommentId())
                .username(SecurityContextHolder.getContext().getAuthentication().getName())
                .build();

    }
}
