package com.example.testlogin.project_java2.mapper;


import com.example.testlogin.project_java2.dto.BankAccountDto;
import com.example.testlogin.project_java2.model.BankAccount;

public class BankAccountMapper {

    public static BankAccountDto mapToBankAccountApiDto(BankAccount bankAccount) {

        BankAccountDto bankAccountDto = BankAccountDto.builder()
                        .id(bankAccount.getId())
                        .code(bankAccount.getCode())
                        .amount(bankAccount.getAmount())
                        .status(bankAccount.getStatus())
                        .userAccount(bankAccount.getUserAccount())
                        .build();

        if (bankAccountDto != null) {

            return bankAccountDto;

        }else{

            System.out.println("" + null);

            return null;
        }
    }

}
