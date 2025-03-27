package com.example.ume.controller;

import com.example.ume.DTO.UserJoinDTO;
import com.example.ume.domain.User;
import com.example.ume.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> signUp(@RequestBody UserJoinDTO userJoinDTO){

        try {
           User user =  userService.signIn(userJoinDTO);

           return ResponseEntity.ok(user);
        }catch (Exception e){


            return ResponseEntity.ok("가입실패");
        }



    }
}
