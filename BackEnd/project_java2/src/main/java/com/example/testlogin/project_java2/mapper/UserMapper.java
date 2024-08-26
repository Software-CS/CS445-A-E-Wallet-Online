package com.example.testlogin.project_java2.mapper;

import com.example.testlogin.project_java2.dto.UserDto;
import com.example.testlogin.project_java2.model.UserAccount;

public class UserMapper {


    public static UserDto  mapToUserApiDto(UserAccount user) {

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .type(user.getType())
                .active(user.getActive())
                .bankAccount(user.getBankAccount())
                .build();

        if (userDto != null) {

            return userDto;

        }else{

            System.out.println("" + null);

            return null;
        }
    }

}
