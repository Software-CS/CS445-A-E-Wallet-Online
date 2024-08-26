package com.example.testlogin.project_java2.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {



    @GetMapping(value = {"/"})
    private String index(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal());
        if(authentication.getPrincipal() == "anonymousUser"){
            return "redirect:/login";
        }
        return "redirect:/swagger-ui/index.html";
    }





}
