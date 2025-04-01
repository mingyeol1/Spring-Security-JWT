package com.example.ume.jwt;

import com.example.ume.DTO.CustomUserDetail;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;


@RequiredArgsConstructor
@Log4j2
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;


    //어떻게 UserDetailService를 불러오는가?
    //1. 로그인시 UsernamePasswordAuthenticationToken이게 호출됨.
    //2. 시큐리티는 DaoAuthenticationProvider를 사용하여 인증을 수행
    //3. DaoAuthenticationProvider 내부에서 UserDetailsService.loadUserByUsername(username)을 호출
    //4. CustomUserDetailService.loadUserByUsername(username)이 실행됨
    //5. 데이터베이스에서 사용자를 조회하고, UserDetails 객체를 반환
    //6. DaoAuthenticationProvider가 비밀번호를 검증
    //7. 검증이 통과되면 인증이 성공하고, CustomUserDetailService가 Authentication 객체를 반환
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        System.out.println(username);
        log.info(username + "유저네이이이임");

        //CustomUserDetailService를 호출 하는 메서드. 직접 호출을 안하면 이게 동작함.
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();

        String username = customUserDetail.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, 60*60*10L);

        response.addHeader("Authorization", "Bearer " + token);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패!!");
    }
}
