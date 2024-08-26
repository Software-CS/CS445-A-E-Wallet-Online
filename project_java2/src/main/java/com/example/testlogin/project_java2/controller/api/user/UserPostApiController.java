package com.example.testlogin.project_java2.controller.api.user;

import com.example.testlogin.project_java2.dto.BankAccountDto;
import com.example.testlogin.project_java2.dto.PaymentDto;
import com.example.testlogin.project_java2.dto.UploadDto;
import com.example.testlogin.project_java2.model.BankAccount;
import com.example.testlogin.project_java2.model.Upload;
import com.example.testlogin.project_java2.model.UserAccount;
import com.example.testlogin.project_java2.model.object.PaymentToken;
import com.example.testlogin.project_java2.security.Security;
import com.example.testlogin.project_java2.security.middleware.JWTAuthenticationFilter;
import com.example.testlogin.project_java2.service.*;
import net.minidev.json.JSONObject;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserPostApiController {


    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;
    private final BankAccountService bankAccountService;
    private final PaymentService paymentService;
    private final UploadService uploadService;
    private final OcrService ocrService;


    @Autowired
    public UserPostApiController(JWTAuthenticationFilter jwtAuthenticationFilter, UserService userService, BankAccountService bankAccountService, PaymentService paymentService, UploadService uploadService, OcrService ocrService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userService = userService;
        this.bankAccountService = bankAccountService;
        this.paymentService = paymentService;
        this.uploadService = uploadService;
        this.ocrService = ocrService;
    }

    @PostMapping("/user_payment")
    private ResponseEntity<JSONObject> payment(@RequestParam("file") MultipartFile file,
    @RequestParam("account_number_sent_to") String account_number_sent_to) throws TesseractException, IOException {

        JSONObject object = new JSONObject();
        Path tempFilePath = null;

        try{
            String tempDir = System.getProperty("java.io.tmpdir");
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

            tempFilePath = Paths.get(tempDir, fileName);
            Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

            String dataScan = ocrService.getImageString(tempFilePath.toFile());
            String inherited_account_number = paymentService.find_Inherited_account_number_test(dataScan);
            String content = paymentService.find_content_test(dataScan);

            UserAccount user_payment = userService.findByEmail(Security.getSessionUser());
            BankAccount bankAccountUser = bankAccountService.findByUserAccount(user_payment);
            UploadDto uploadDto = null;

            if(!paymentService.check_inherited_account_number(inherited_account_number)
            || !paymentService.check_content(content)){
                object.put("error","Your bank code invalid or payment content is invalid!");
                object.put("payment_status",false);
                return new ResponseEntity<>(object, HttpStatus.BAD_REQUEST);
            }
            if(bankAccountUser != null){
                uploadDto = uploadService.create(file);
            }
            PaymentDto paymentDto = paymentService.create(tempFilePath.toFile(), object,
            uploadDto, bankAccountUser, account_number_sent_to);

            if(paymentDto.isStatus_increase_amount()){
                object.put("message","Payment successfully");
                object.put("user_payment",paymentDto);
                return new ResponseEntity<>(object, HttpStatus.OK);
            }
            object.put("message","Payment failed!!!");
            object.put("user_payment",paymentDto);
            return new ResponseEntity<>(object, HttpStatus.BAD_REQUEST);
        }catch (Exception exception){
            object.put("error", exception.getMessage());
            return new ResponseEntity<>(object, HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            // Xóa tệp tạm thời nếu nó tồn tại
            if (tempFilePath != null && Files.exists(tempFilePath)) {
                try {
                    Files.delete(tempFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }






}
