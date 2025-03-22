package org.example.forumservice.service.user;

import lombok.RequiredArgsConstructor;
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
}
