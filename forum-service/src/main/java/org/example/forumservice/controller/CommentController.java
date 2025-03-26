package org.example.forumservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.forumservice.dto.comment.CommentDto;
import org.example.forumservice.dto.comment.CreateCommentDto;
import org.example.forumservice.dto.comment.DeleteCommentDto;
import org.example.forumservice.dto.comment.GetCommentsByIssueDto;
import org.example.forumservice.service.comment.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/getByIssue")
    public List<CommentDto> getByIssue(@Valid @RequestBody GetCommentsByIssueDto dto) {
        return commentService.getByIssue(dto);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@Valid @RequestBody CreateCommentDto dto) {
        commentService.create(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> delete(@Valid @RequestBody DeleteCommentDto dto) {
        commentService.delete(dto);
        return ResponseEntity.noContent().build();
    }
}
