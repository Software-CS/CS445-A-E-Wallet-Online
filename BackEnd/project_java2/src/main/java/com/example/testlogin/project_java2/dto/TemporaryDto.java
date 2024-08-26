package com.example.testlogin.project_java2.dto;

import com.example.testlogin.project_java2.model.UserAccount;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class TemporaryDto {

    private String id;
    private String payment_token;
    private UserAccount userAccount;

}
