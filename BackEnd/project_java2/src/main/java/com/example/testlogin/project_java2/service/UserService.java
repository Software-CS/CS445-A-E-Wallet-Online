package com.example.testlogin.project_java2.service;

import com.example.testlogin.project_java2.dto.UserDto;
import com.example.testlogin.project_java2.model.UserAccount;

import java.util.List;

public interface UserService {

    int countByEmail(String email);
    UserDto createUser(UserDto userDto);

    UserDto update_name_user(String name);

    List<UserDto>  listUsersApi();

    UserAccount findByEmail(String email);

    boolean checkActiveUser();

    void sendMailToVerifyUser(UserDto userDto);

    boolean verifyAccount(String email, String password,String token);

    int countVerifyAccountByEmail(String email);

}
