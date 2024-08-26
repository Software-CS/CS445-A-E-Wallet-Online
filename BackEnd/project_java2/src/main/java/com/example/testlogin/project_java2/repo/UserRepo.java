package com.example.testlogin.project_java2.repo;

import com.example.testlogin.project_java2.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo  extends JpaRepository<UserAccount, String> {

    UserAccount findByEmail(String email);
    int countByEmail(String email);

}
