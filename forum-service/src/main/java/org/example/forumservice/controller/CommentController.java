package org.example.forumservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.forumservice.dto.comment.CommentDto;
import org.example.forumservice.dto.comment.CreateCommentDto;
import org.example.forumservice.dto.comment.DeleteCommentDto;
import org.example.forumservice.dto.comment.GetByIssueDto;
import org.example.forumservice.model.Comment;
import org.example.forumservice.service.comment.CommentService;
import org.example.forumservice.util.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public List<CommentDto> getByIssue(@Valid @RequestBody GetByIssueDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException(bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        return commentService.getByIssue(dto).stream().map(CommentController::toCommentDto).toList();
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@Valid @RequestBody CreateCommentDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException(bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        commentService.create(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> delete(@Valid @RequestBody DeleteCommentDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException(bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        commentService.delete(dto);
        return ResponseEntity.noContent().build();
    }

    private static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorUsername(comment.getAuthor().getUsername())
                .createdAt(comment.getCreatedAt()).build();
    }
}
