package org.example.forumservice.util.issue;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.forumservice.repo.IssueRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IssueExistsInDBValidator implements ConstraintValidator<IssueExists, Long> {
    private final IssueRepository issueRepository;

    @Override
    public boolean isValid(Long issueId, ConstraintValidatorContext constraintValidatorContext) {
        if(issueId == null) return false;
        return issueRepository.existsById(issueId);
    }
}
