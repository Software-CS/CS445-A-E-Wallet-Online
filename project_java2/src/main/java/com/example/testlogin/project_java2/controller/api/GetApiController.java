package com.example.testlogin.project_java2.controller.api;


import com.example.testlogin.project_java2.dto.UserDto;
import com.example.testlogin.project_java2.security.Security;
import com.example.testlogin.project_java2.service.UserService;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class GetApiController {

    UserService userService;

    @GetMapping("/hello")
    private ResponseEntity <JSONObject> index(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<String> list = new ArrayList<>();
        list.add("Toi la tuong");
        JSONObject object = new JSONObject();
        object.put("message","Hello world! You are " + authentication.getName());
        object.put("API-management","https://project-java2.onrender.com/swagger-ui/index.html");
        object.put("owner",list);

        return new ResponseEntity<>(object, HttpStatus.OK);
    }








}
