package org.example.forumservice.util.comment;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.forumservice.repo.CommentRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentExistsInDBValidator implements ConstraintValidator<CommentExists, Long> {
    private final CommentRepository commentRepository;

    @Override
    public boolean isValid(Long commentId, ConstraintValidatorContext constraintValidatorContext) {
        if (commentId == null) return false;
        return commentRepository.existsById(commentId);
    }
}
