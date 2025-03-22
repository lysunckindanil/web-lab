package org.example.webapp.controller.forum;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.webapp.dto.forum.comment.CreateCommentDto;
import org.example.webapp.dto.forum.comment.DeleteCommentDto;
import org.example.webapp.service.CommentService;
import org.example.webapp.util.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/forum/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<Void> create(@Valid CreateCommentDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        }
        commentService.create(dto);
        return ResponseEntity.noContent().build();
    }

    @ResponseBody
    @PostMapping("/delete")
    public ResponseEntity<Void> delete(@Valid DeleteCommentDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        }
        commentService.delete(dto);
        return ResponseEntity.noContent().build();
    }
}
