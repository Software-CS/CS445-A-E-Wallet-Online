package com.example.testlogin.project_java2.service;

import com.example.testlogin.project_java2.dto.TemporaryDto;
import com.example.testlogin.project_java2.model.UserAccount;

import java.time.LocalDateTime;

public interface TemporaryService {

    void deleteOldPaymentToken(LocalDateTime timeLimit);

    int count();

    int countTemporariesByUserAccount(UserAccount userAccount);

    TemporaryDto savePaymentToken(String token);
    void deletePaymentTokenByUserId(String user_id);
    String findPaymentTokenByUserId(String user_id);

}
