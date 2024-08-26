package com.example.testlogin.project_java2.repo;

import com.example.testlogin.project_java2.model.object.VerifyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

public interface VerifyAccountRepo extends JpaRepository<VerifyAccount,Long> {


    @Query(value ="SELECT COUNT(*) FROM verify_account_temporaries " +
                  "WHERE verify_account_temporaries.email = :email " +
                  "AND verify_account_temporaries.verify_token = :token", nativeQuery = true)
    int countVerifyAccountByEmailAndVerifyToken(@Param("email") String email,@Param("token") String token);

    int countVerifyAccountByEmail(String email);
    @Transactional
    @Modifying
    @Query(value ="DELETE FROM verify_account_temporaries " +
                  "WHERE verify_account_temporaries.email = :email", nativeQuery = true)
    void deleteVerifyAccountEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM VerifyAccount v WHERE v.createdAt < :timeLimit")
    void deleteByCreatedAtBefore(@Param("timeLimit") LocalDateTime timeLimit);
}
