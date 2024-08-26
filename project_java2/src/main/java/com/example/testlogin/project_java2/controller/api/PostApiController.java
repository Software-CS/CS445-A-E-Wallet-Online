package com.example.testlogin.project_java2.controller.api;


import com.example.testlogin.project_java2.dto.UserDto;
import com.example.testlogin.project_java2.service.UserService;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PostApiController {

    UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(PostApiController.class);


    @PostMapping("/create/user")
    private ResponseEntity<JSONObject> index(
            @Valid @RequestBody UserDto userDto,
            BindingResult res){

        logger.info("Received POST request at /api/v1/create/user");
        logger.info("UserDto: " + userDto);
        JSONObject object = new JSONObject();

        if (res.hasErrors()) {
            object.put("message", "error");
            object.put("error", res.getAllErrors());
            return new ResponseEntity<>(object, HttpStatus.BAD_REQUEST);
        }
        if(userService.countByEmail(userDto.getEmail()) >= 1){
            object.put("message", "This account already exists!");
            object.put("error", res.getAllErrors());
            return new ResponseEntity<>(object, HttpStatus.BAD_REQUEST);
        }
        if(userService.countVerifyAccountByEmail(userDto.getEmail()) >= 1){
            object.put("message", "You did sign up for this email and wait for a while to sign up again!");
            object.put("error", res.getAllErrors());
            return new ResponseEntity<>(object, HttpStatus.BAD_REQUEST);
        }
        try{
            userService.sendMailToVerifyUser(userDto);
            object.put("message", "Next step enter your token to do the verify account");
            object.put("new_user", userDto);

            return new ResponseEntity<>(object, HttpStatus.OK);
        }catch (Exception error){
            object.put("error", error);
            return new ResponseEntity<>(object, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/verify/user")
    private ResponseEntity<?> verify_account(@RequestParam("email") String email,
                                             @RequestParam("password") String password,
                                             @RequestParam("verify_token") String verify_token){
        JSONObject object = new JSONObject();
        try{
            if(email != null || password != null || verify_token != null){
                if(userService.verifyAccount(email, password, verify_token)){
                    object.put("message", "Verify successfully, login to your account!");
                    return new ResponseEntity<>(object, HttpStatus.OK);
                }
            }
            object.put("message", "Verify failed");
            return new ResponseEntity<>(object, HttpStatus.BAD_REQUEST);
        }catch (Exception error){
            object.put("error", error);
            return new ResponseEntity<>(object, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
