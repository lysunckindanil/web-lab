package org.example.webapp.service;

import org.example.webapp.dto.UserDto;
import org.example.webapp.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService{
    List<User> findAll();

    @Transactional
    void register(UserDto dto);

    User findByUsername(String username) throws UsernameNotFoundException;

    void grantAuthority(String authority, String username);

    void revokeAuthority(String authority, String username);
}
