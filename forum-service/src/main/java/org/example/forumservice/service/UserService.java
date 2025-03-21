package org.example.forumservice.service;

import lombok.RequiredArgsConstructor;
import org.example.forumservice.entity.User;
import org.example.forumservice.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
