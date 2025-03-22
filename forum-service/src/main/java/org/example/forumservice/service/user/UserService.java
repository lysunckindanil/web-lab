package org.example.forumservice.service.user;

import org.example.forumservice.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
}
