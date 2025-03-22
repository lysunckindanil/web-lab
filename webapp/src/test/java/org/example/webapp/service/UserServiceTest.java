package org.example.webapp.service;

import org.example.webapp.model.User;
import org.example.webapp.repo.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@ActiveProfiles("test")
@SpringBootTest
class UserServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void loadUserByUsername() {
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        assertNotNull(userDetails);
        Assertions.assertTrue(userDetails.isEnabled());
    }
}