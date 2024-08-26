package com.example.testlogin.project_java2.service.worker;


import com.example.testlogin.project_java2.dto.UploadDto;
import com.example.testlogin.project_java2.mapper.UploadMapper;
import com.example.testlogin.project_java2.model.Upload;
import com.example.testlogin.project_java2.repo.UploadRepo;
import com.example.testlogin.project_java2.repo.UserRepo;
import com.example.testlogin.project_java2.security.Security;
import com.example.testlogin.project_java2.service.UploadService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UploadWorker implements UploadService {

    private  UploadRepo uploadRepo;
    private UserRepo userRepo;


    @Override
    public List<UploadDto> listUploadApi() {


        List<Upload> uploads = uploadRepo.findAll();

        return uploads.stream()
            .map(UploadMapper::mapToUploadApiDto)
            .collect(Collectors.toList());
    }


    @Override
    public UploadDto create(MultipartFile file) {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if(fileName.contains("..")){
                throw new Exception("The file name is invalid" + fileName);
            }
            Upload fileUpload = new Upload(fileName, file.getContentType(), file.getBytes());
            UploadDto uploadDto =  new UploadDto();
            fileUpload.setUserAccount(userRepo.findByEmail(Security.getSessionUser()));
            Upload newFileUpload = uploadRepo.save(fileUpload);

            uploadDto.setId(newFileUpload.getId());
            uploadDto.setFile_name(newFileUpload.getFile_name());
            uploadDto.setFile_type(newFileUpload.getFile_type());
            uploadDto.setData(newFileUpload.getData());
            uploadDto.setUserAccount(newFileUpload.getUserAccount());

            return uploadDto;
        } catch (Exception e) {
            System.out.println("error: "+e);
            throw new RuntimeException("File could not be save!");
        }
    }

    @Override
    public Upload downloadFile(String fileId, String user_id) throws Exception {
        return uploadRepo.findByIdAndUserAccountId(fileId, user_id);
    }

    @Override
    public Upload findById(String id) {

        Optional<Upload> upload = uploadRepo.findById(id);
        return upload.orElse(null);
    }

}
