package com.example.testlogin.project_java2.dto;

import com.example.testlogin.project_java2.constant.EnumConstant;
import com.example.testlogin.project_java2.model.BankAccount;
import com.example.testlogin.project_java2.model.Upload;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private String id;
    @NotEmpty(message = "Email should not be empty!")
    @Email(message = "Email should be valid!")
    private String email;
    private String name;
    @NotEmpty(message = "Password should not be empty!")
    private String password;
    private EnumConstant type;
    private EnumConstant active;
    private BankAccount bankAccount;
    private List<Upload> uploads = new ArrayList<>();

}
