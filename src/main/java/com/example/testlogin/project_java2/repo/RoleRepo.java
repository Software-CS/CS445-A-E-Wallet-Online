package com.example.testlogin.project_java2.repo;

import com.example.testlogin.project_java2.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Long> {

    Role findByName(String name);
}
