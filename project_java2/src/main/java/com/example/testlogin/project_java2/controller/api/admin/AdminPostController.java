package com.example.testlogin.project_java2.controller.api.admin;
import com.example.testlogin.project_java2.service.*;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@RestController
@RequestMapping("/api/admin")
public class AdminPostController {

    private final UserService userService;
    private final BankAccountService bankAccountService;
    private final PaymentService paymentService;
    private final UploadService uploadService;
    private final OcrService ocrService;

    @Autowired
    public AdminPostController(UserService userService, BankAccountService bankAccountService, PaymentService paymentService, UploadService uploadService, OcrService ocrService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
        this.paymentService = paymentService;
        this.uploadService = uploadService;
        this.ocrService = ocrService;
    }

    @PostMapping("/test_scan_image")
    private ResponseEntity<?> test_scan_image(@RequestParam("file") MultipartFile file){

        JSONObject object = new JSONObject();
        Path tempFilePath = null;
        String contentType = file.getContentType();

        if (contentType == null || !(contentType.equalsIgnoreCase("image/jpeg") ||
            contentType.equalsIgnoreCase("image/png") ||
            contentType.equalsIgnoreCase("image/jpg"))) {
            object.put("error", "Tệp không phải là hình ảnh hợp lệ.");
            return new ResponseEntity<>(object, HttpStatus.BAD_REQUEST);
        }
        try{
            String tempDir = System.getProperty("java.io.tmpdir");
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            tempFilePath = Paths.get(tempDir, fileName);
            Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

            String dataScan = ocrService.getImageString(tempFilePath.toFile());

            double amount = paymentService.find_amount_test(dataScan);
            String content = paymentService.find_content_test(dataScan);
            String inherited_account_number = paymentService.find_Inherited_account_number_test(dataScan);
            System.out.println(dataScan);
            System.out.println("Amount: " + amount);
            System.out.println("Content: " + content);
            System.out.println("Inherited account number: " + inherited_account_number);

            return new ResponseEntity<>(object, HttpStatus.OK);
        }catch (Exception exception){
            object.put("error", exception.getMessage());
            return new ResponseEntity<>(object, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
