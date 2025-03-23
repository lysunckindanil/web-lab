package org.example.webapp.service;

import lombok.RequiredArgsConstructor;
import org.example.webapp.dto.UserDto;
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
    public void register(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.getRoles().add(roleRepository.findByName("ROLE_USER"));
        userRepository.save(user);
    }
}
