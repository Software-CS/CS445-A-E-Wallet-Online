package com.example.testlogin.project_java2.service;

import com.example.testlogin.project_java2.dto.UploadDto;
import com.example.testlogin.project_java2.model.Upload;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UploadService {



    List<UploadDto> listUploadApi();

    UploadDto create(MultipartFile file) throws IOException;
    Upload downloadFile(String fileId, String user_id) throws Exception;
    Upload findById(String id);
}
