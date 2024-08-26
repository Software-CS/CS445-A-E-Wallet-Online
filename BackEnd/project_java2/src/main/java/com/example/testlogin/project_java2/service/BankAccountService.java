package com.example.testlogin.project_java2.service;

import com.example.testlogin.project_java2.dto.BankAccountDto;
import com.example.testlogin.project_java2.dto.UserDto;
import com.example.testlogin.project_java2.model.BankAccount;
import com.example.testlogin.project_java2.model.UserAccount;

import java.util.List;

public interface BankAccountService {


    BankAccountDto active_bank_account(UserAccount account);

    List<BankAccountDto> listBankAccountApi();

    BankAccountDto getBankAccountDtoByUser(UserAccount userAccount);

    BankAccount findByUserAccount(UserAccount userAccount);

}
