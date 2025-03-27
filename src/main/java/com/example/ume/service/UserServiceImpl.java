package com.example.ume.service;

import com.example.ume.DTO.UserJoinDTO;
import com.example.ume.domain.User;
import com.example.ume.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public User signIn(UserJoinDTO userJoinDTO) {

        User user = new User();

        user.setUsername(userJoinDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userJoinDTO.getPassword()));
        user.setRole("ROLE_USER");

        userRepository.save(user);
        return user;
    }
}
