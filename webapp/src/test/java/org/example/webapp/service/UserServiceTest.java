package org.example.webapp.service;

import org.example.webapp.dto.UserDto;
import org.example.webapp.model.Role;
import org.example.webapp.model.User;
import org.example.webapp.repo.RoleRepository;
import org.example.webapp.repo.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        userRepository.deleteAll();
        roleRepository.deleteAll();

        roleRepository.save(new Role("ROLE_USER"));
    }

    @Test
    void findByUsername() {
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);
        Optional<User> userOptional = userService.findByUsername(user.getUsername());
        assertTrue(userOptional.isPresent());
        assertEquals(user.getUsername(), userOptional.get().getUsername());
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

    @Test
    void register_has_ROLE_USER() {
        UserDto userDto = UserDto.builder()
                .username("user")
                .password("password")
                .build();
        userService.register(userDto);
        assertEquals("ROLE_USER", userService.findByUsername("user")
                .get().getRoles().getFirst().getName());
    }

    @Test
    void grantAuthority_Call_Grants() {
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);

        userService.grantAuthority("ROLE_USER", "user");

        assertEquals(1, userService.findByUsername("user")
                .get().getRoles().size());
        assertEquals("ROLE_USER", userService.findByUsername("user")
                .get().getRoles().getFirst().getName());
    }


    @Test
    void revokeAuthority() {
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("password"));
        user.getRoles().add(roleRepository.findByName("ROLE_USER"));
        userRepository.save(user);

        userService.revokeAuthority("ROLE_USER", "user");

        assertEquals(0, userService.findByUsername("user")
                .get().getRoles().size());
    }
}