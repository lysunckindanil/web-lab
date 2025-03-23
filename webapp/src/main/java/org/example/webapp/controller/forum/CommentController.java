package org.example.webapp.controller.forum;

import lombok.RequiredArgsConstructor;
import org.example.webapp.dto.forum.comment.CreateCommentDto;
import org.example.webapp.dto.forum.comment.DeleteCommentDto;
import org.example.webapp.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/forum/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<Void> create(CreateCommentDto dto) {
        commentService.create(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> delete(DeleteCommentDto dto) {
        commentService.delete(dto);
        return ResponseEntity.noContent().build();
    }
}
