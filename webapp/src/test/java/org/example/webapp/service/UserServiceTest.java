package org.example.webapp.service;

import org.example.webapp.dto.UserDto;
import org.example.webapp.model.Role;
import org.example.webapp.model.User;
import org.example.webapp.repo.RoleRepository;
import org.example.webapp.repo.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeAll
    public void setUp() {
        roleRepository.save(new Role("ROLE_USER"));
    }

    @Test
    void loadUserByUsername() {
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);
        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        assertNotNull(userDetails);
        Assertions.assertTrue(userDetails.isEnabled());
    }

    @Test
    void register() {
        UserDto userDto = UserDto.builder()
                .username("user")
                .password("password")
                .build();
        userService.register(userDto);
        assertTrue(userRepository.getByUsername("user").isPresent());
    }
}