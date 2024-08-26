package com.example.testlogin.project_java2.mapper;

import com.example.testlogin.project_java2.dto.PaymentDto;
import com.example.testlogin.project_java2.dto.UploadDto;
import com.example.testlogin.project_java2.model.Payment;
import com.example.testlogin.project_java2.model.Upload;

public class PaymentMapper {

    public static PaymentDto mapToPaymentApiDto(Payment payment) {

        PaymentDto paymentDto = PaymentDto.builder()
                .id(payment.getId())
                .user_bank_code(payment.getUser_bank_code())
                .start_time(payment.getStart_time())
                .deposit_amount(payment.getDeposit_amount())
                .payment_content(payment.getPayment_content())
                .bankAccount(payment.getBankAccount())
                .upload(payment.getUpload())
                .build();

        if (paymentDto != null) {

            return paymentDto;

        }else{

            System.out.println("" + null);

            return null;
        }
    }
}
