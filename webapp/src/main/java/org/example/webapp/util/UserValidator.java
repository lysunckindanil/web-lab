package org.example.webapp.util;

import lombok.RequiredArgsConstructor;
import org.example.webapp.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {
    private final UserDetailsService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto userDto = (UserDto) target;
        if (userService.loadUserByUsername(userDto.getUsername()) != null) {
            errors.rejectValue("username", "", "Username is used by another user.");
        }
    }
}
