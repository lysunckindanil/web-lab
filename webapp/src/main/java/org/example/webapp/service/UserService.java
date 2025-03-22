package org.example.webapp.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.webapp.model.Role;
import org.example.webapp.model.User;
import org.example.webapp.repo.RoleRepository;
import org.example.webapp.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        if (roleRepository.findByName("ROLE_ADMIN") == null) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }
        if (roleRepository.findByName("ROLE_USER") == null) {
            roleRepository.save(new Role("ROLE_USER"));
        }

        if (userRepository.getByUsername("admin").isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("secret"));
            user.getRoles().add(roleRepository.findByName("ROLE_ADMIN"));
            user.getRoles().add(roleRepository.findByName("ROLE_USER"));
            userRepository.save(user);
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.getByUsername(username);
        if (userOptional.isEmpty()) throw new UsernameNotFoundException(username);
        return userOptional.get();
    }

    @Transactional
    public void register(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.getRoles().add(roleRepository.findByName("ROLE_USER"));
        userRepository.save(user);
    }
}
