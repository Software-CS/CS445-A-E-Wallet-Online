package com.example.testlogin.project_java2.dto;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserResponseDto implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;
    String id;
    String accessToken;
    String tokenType = "Bearer ";
    Object object;

    public UserResponseDto(String id, String accessToken, Object object) {
        this.id = id;
        this.accessToken = accessToken;
        this.object = object;
    }
}
