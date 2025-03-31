package com.example.ume.service;

import com.example.ume.DTO.CustomUserDetail;
import com.example.ume.domain.User;
import com.example.ume.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;


    //DB에 저장된 사용자를 조회하는 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // role이 null이 아닐 경우만 설정, 기본값은 "ROLE_USER"로 설정
        String role = user.getRole() != null ? user.getRole() : "ROLE_USER";

        //CustomUserDetail 생성된 객체반환
        return new CustomUserDetail(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(role)) // 단일 권한 리스트로 반환
        );
    }

}
