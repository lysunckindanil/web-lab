package org.example.forumservice.service.user;

import lombok.RequiredArgsConstructor;
import org.example.forumservice.model.Role;
import org.example.forumservice.model.User;
import org.example.forumservice.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean isRedactor(String username) {
        User user = findByUsername(username).get();
        return user.getRoles().stream().map(Role::getName).anyMatch(role -> role.equals("ROLE_REDACTOR") || role.equals("ROLE_ADMIN"));
    }
}
