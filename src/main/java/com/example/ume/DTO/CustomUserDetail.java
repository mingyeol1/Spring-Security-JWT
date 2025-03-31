package com.example.ume.DTO;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

//User 와 UserDetails의 차이
//UserDetails는 내가 모든 설정을 해야해서 코드가 길어지지만 원하는 설정가능 /ex 계정 만료 여부 계정 잠금 여부 등.
//User는 UserDetails를 이미 implements하고 있어서 그냥 불러다가 쓰면 되서 매우 간편함
public class CustomUserDetail extends User {



    public CustomUserDetail(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
}




