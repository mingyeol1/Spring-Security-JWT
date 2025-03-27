package com.example.ume.service;

import com.example.ume.DTO.UserJoinDTO;
import com.example.ume.domain.User;

public interface UserService {
    User signIn(UserJoinDTO userJoinDTO);
}
