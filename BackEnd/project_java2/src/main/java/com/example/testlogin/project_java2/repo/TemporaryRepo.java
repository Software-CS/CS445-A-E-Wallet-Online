package com.example.testlogin.project_java2.repo;

import com.example.testlogin.project_java2.model.Temporary;
import com.example.testlogin.project_java2.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

public interface TemporaryRepo extends JpaRepository<Temporary,String> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Temporary t WHERE t.createdAt < :timeLimit")
    void deleteByCreatedAtBefore(@Param("timeLimit") LocalDateTime timeLimit);

    int countTemporariesByUserAccount(UserAccount userAccount);

    @Transactional
    @Modifying
    @Query(value ="DELETE FROM temporaries WHERE user_id = :user_id", nativeQuery = true)
    void deletePaymentTokenByUserId(@Param("user_id") String user_id);

    @Query(value ="SELECT temporaries.payment_token FROM temporaries WHERE user_id = :user_id", nativeQuery = true)
    String findPaymentTokenByUserId(@Param("user_id") String user_id);
}
