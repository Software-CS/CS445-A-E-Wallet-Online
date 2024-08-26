package com.example.testlogin.project_java2.controller.api.user.product;


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
public class UserProductApiController {

    @GetMapping("/products")
    private ResponseEntity<JSONObject> index(){

        JSONObject object = new JSONObject();
        object.put("message","Fetch all products");

        return new ResponseEntity<>(object, HttpStatus.OK);
    }

}
