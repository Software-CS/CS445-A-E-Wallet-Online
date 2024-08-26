package com.example.testlogin.project_java2.repo;

import com.example.testlogin.project_java2.model.BankAccount;
import com.example.testlogin.project_java2.model.Role;
import com.example.testlogin.project_java2.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface BankAccountRepo extends JpaRepository<BankAccount,String> {

    BankAccount findByUserAccount(UserAccount userAccount);


    @Modifying
    @Query(value ="UPDATE bank_account " +
                  "SET code = :code, amount = amount + :amount " +
                  "WHERE id = :bank_account_id", nativeQuery = true)
    void updateAmountByUser(@Param("bank_account_id") String bankAccountId,
                            @Param("code") String code,
                            @Param("amount") double amount);


}
