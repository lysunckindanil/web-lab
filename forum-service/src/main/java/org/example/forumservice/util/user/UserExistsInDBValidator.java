package org.example.forumservice.util.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.forumservice.service.user.UserService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserExistsInDBValidator implements ConstraintValidator<UserExists, String> {
    private final UserService userService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if (username == null) return false;
        return userService.findByUsername(username).isPresent();
    }
}
