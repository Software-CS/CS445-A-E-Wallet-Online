package com.example.testlogin.project_java2.dto;


import com.example.testlogin.project_java2.model.Payment;
import com.example.testlogin.project_java2.model.UserAccount;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UploadDto {

    private String id;
    @NotEmpty(message = "file_name should not be empty!")
    private String file_name;
    @NotEmpty(message = "file_type should not be empty!")
    private String file_type;
    @NotEmpty(message = "data should not be empty!")
    private byte[] data;
    private UserAccount userAccount;
    private List<Payment> payments = new ArrayList<>();



}
