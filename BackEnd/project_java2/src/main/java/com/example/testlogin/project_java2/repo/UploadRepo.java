package com.example.testlogin.project_java2.repo;

import com.example.testlogin.project_java2.model.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadRepo extends JpaRepository<Upload, String> {


    Upload findByIdAndUserAccountId(String id, String user_id);


}
