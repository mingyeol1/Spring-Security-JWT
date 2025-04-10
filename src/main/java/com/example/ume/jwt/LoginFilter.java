package com.example.ume.jwt;

import com.example.ume.DTO.CustomUserDetail;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
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

        //유저정보
        String username = authentication.getName();

        Collection<? extends  GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        
        //토큰생성
        String access = jwtUtil.createJwt("access", username, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", username, role, 8640000L);

        //응답설정
        response.setHeader("access", access);
        response.addCookie(createCooKie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    private Cookie createCooKie(String key, String value){  //첫번째 인자는 키값, 두번째인자는 JWT가 들어갈 인자값

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60); //쿠키의 생명주기 JWT토큰과 같은 값
//        cookie.setSecure(true);     //HTTPS통신을 할경우 
//        cookie.setPath("/");        //쿠키가 지정될 범위
        cookie.setHttpOnly(true);     //클라이언트쪽에서 자바스크립트가 쿠키에 접근하지 못하도록

        return cookie;

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패!!");
    }
}
