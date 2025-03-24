package org.example.webapp.service;

import lombok.RequiredArgsConstructor;
import org.example.webapp.dto.UserDto;
import org.example.webapp.model.User;
import org.example.webapp.repo.RoleRepository;
import org.example.webapp.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUsername(String username){
        return userRepository.getByUsername(username);
    }

    @Transactional
    @Override
    public void register(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.getRoles().add(roleRepository.findByName("ROLE_USER"));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void grantAuthority(String authority, String username) {
        Optional<User> userOptional = userRepository.getByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getRoles().add(roleRepository.findByName(authority));
            userRepository.save(user);
        }
    }

    @Transactional
    @Override
    public void revokeAuthority(String authority, String username) {
        Optional<User> userOptional = userRepository.getByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.getRoles().remove(roleRepository.findByName(authority));
            userRepository.save(user);
        }
    }
}
