package com.example.ume.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
public class MainController {

    @GetMapping("/")
    public String mainPage(){

        return "MainPage";
    }

    @GetMapping("/admin")
    public String admin(){

        return "AdminPage";
    }
}
