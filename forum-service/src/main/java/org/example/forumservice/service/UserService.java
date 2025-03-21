package org.example.forumservice.service;

import org.example.forumservice.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
}
