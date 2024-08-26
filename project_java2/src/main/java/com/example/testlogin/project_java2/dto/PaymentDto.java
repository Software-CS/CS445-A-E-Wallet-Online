package com.example.testlogin.project_java2.dto;


import com.example.testlogin.project_java2.model.BankAccount;
import com.example.testlogin.project_java2.model.Upload;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class PaymentDto {


    private String id;
    private String user_bank_code;
    private LocalDateTime start_time;
    private double deposit_amount;
    private String payment_content;
    private BankAccount bankAccount;
    private Upload upload;

    public PaymentDto(boolean status_check_inherited_account_number, boolean status_check_content, boolean status_increase_amount) {
        this.status_check_inherited_account_number = status_check_inherited_account_number;
        this.status_check_content = status_check_content;
        this.status_increase_amount = status_increase_amount;
    }

    @Getter
    private boolean status_check_inherited_account_number;
    @Getter
    private boolean status_check_content;
    @Getter
    private boolean status_increase_amount;//increase_amount

}
