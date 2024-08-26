package com.example.testlogin.project_java2.controller.api.user;


import com.example.testlogin.project_java2.dto.UserDto;
import com.example.testlogin.project_java2.security.middleware.JWTAuthenticationFilter;
import com.example.testlogin.project_java2.service.UserService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserUpdateApiController {


    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    @Autowired
    public UserUpdateApiController(JWTAuthenticationFilter jwtAuthenticationFilter, UserService userService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userService = userService;
    }

    @PutMapping("/update-name-user")
    private ResponseEntity<JSONObject> update_name_user(@RequestParam("name") String name){
        JSONObject object = new JSONObject();
        try{
            UserDto userDto =  userService.update_name_user(name);
            object.put("user_updated", userDto);

            return new ResponseEntity<>(object, HttpStatus.OK);
        }catch (Exception exception){
            object.put("error",exception);
            return new ResponseEntity<>(object, HttpStatus.OK);
        }
    }





}
