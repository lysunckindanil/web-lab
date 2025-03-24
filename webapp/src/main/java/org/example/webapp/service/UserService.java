package org.example.webapp.service;

import org.example.webapp.dto.UserDto;
import org.example.webapp.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    void register(UserDto dto);

    Optional<User> findByUsername(String username);

    void grantAuthority(String authority, String username);

    void revokeAuthority(String authority, String username);
}
