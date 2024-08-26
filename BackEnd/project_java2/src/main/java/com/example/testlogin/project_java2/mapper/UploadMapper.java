package com.example.testlogin.project_java2.mapper;

import com.example.testlogin.project_java2.dto.UploadDto;
import com.example.testlogin.project_java2.dto.UserDto;
import com.example.testlogin.project_java2.model.Upload;
import com.example.testlogin.project_java2.model.UserAccount;

public class UploadMapper {



    public static UploadDto mapToUploadApiDto(Upload upload) {

        UploadDto uploadDto = UploadDto.builder()
                .id(upload.getId())
                .file_name(upload.getFile_name())
                .file_type(upload.getFile_type())
                .data(upload.getData())
                .userAccount(upload.getUserAccount())
                .build();

        if (uploadDto != null) {

            return uploadDto;

        }else{

            System.out.println("" + null);

            return null;
        }
    }



}
