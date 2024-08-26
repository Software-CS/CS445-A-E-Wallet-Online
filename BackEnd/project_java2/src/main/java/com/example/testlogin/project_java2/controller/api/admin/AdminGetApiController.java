package com.example.testlogin.project_java2.controller.api.admin;

import com.example.testlogin.project_java2.dto.BankAccountDto;
import com.example.testlogin.project_java2.dto.PaymentDto;
import com.example.testlogin.project_java2.dto.UserDto;
import com.example.testlogin.project_java2.dto.UserResponseDto;
import com.example.testlogin.project_java2.model.Upload;
import com.example.testlogin.project_java2.security.middleware.JWTAuthenticationFilter;
import com.example.testlogin.project_java2.service.BankAccountService;
import com.example.testlogin.project_java2.service.PaymentService;
import com.example.testlogin.project_java2.service.UploadService;
import com.example.testlogin.project_java2.service.UserService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminGetApiController {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;
    private final BankAccountService bankAccountService;
    private final PaymentService paymentService;
    private final UploadService uploadService;

    @Autowired
    public AdminGetApiController(JWTAuthenticationFilter jwtAuthenticationFilter, UserService userService, BankAccountService bankAccountService, PaymentService paymentService, UploadService uploadService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userService = userService;
        this.bankAccountService = bankAccountService;
        this.paymentService = paymentService;
        this.uploadService = uploadService;
    }

    @GetMapping("/profile")
    private ResponseEntity<JSONObject> admin(
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest request
    ){
        JSONObject object = new JSONObject();
        try{
            String token = jwtAuthenticationFilter.getJWTFromRequest(request);
            String userId = userService.findByEmail(userDetails.getUsername()).getId();
            UserResponseDto auth = new UserResponseDto(userId, token, userDetails);

            object.put("user", auth);

            return new ResponseEntity<>(object, HttpStatus.OK);
        }catch (Exception exception){

            object.put("error",exception);
            return new ResponseEntity<>(object, HttpStatus.OK);
        }
    }

    @GetMapping("/users")
    private ResponseEntity <JSONObject> users(){

        try{
            JSONObject object = new JSONObject();
            List<UserDto> userDtos = userService.listUsersApi();
            object.put("users",userDtos);

            return new ResponseEntity<>(object, HttpStatus.OK);
        }catch (Exception exception){

            JSONObject object = new JSONObject();
            object.put("error",exception);
            return new ResponseEntity<>(object, HttpStatus.OK);
        }
    }

    @GetMapping("/bank_accounts")
    private ResponseEntity <JSONObject> bank_accounts(){

        try{
            JSONObject object = new JSONObject();
            List<BankAccountDto> bankAccountDtoList = bankAccountService.listBankAccountApi();
            object.put("bank_accounts",bankAccountDtoList);

            return new ResponseEntity<>(object, HttpStatus.OK);
        }catch (Exception exception){

            JSONObject object = new JSONObject();
            object.put("error",exception);
            return new ResponseEntity<>(object, HttpStatus.OK);
        }
    }


    @GetMapping("/payments")
    private ResponseEntity <JSONObject> payments(){

        try{
            JSONObject object = new JSONObject();
            List<PaymentDto> paymentDtos = paymentService.listPaymentApi();
            object.put("payments",paymentDtos);

            return new ResponseEntity<>(object, HttpStatus.OK);
        }catch (Exception exception){

            JSONObject object = new JSONObject();
            object.put("error",exception);
            return new ResponseEntity<>(object, HttpStatus.OK);
        }
    }


    @GetMapping("/download/{fileId}/{user_id}")
    private ResponseEntity<?> download(
            @PathVariable("fileId")String fileId,
            @PathVariable("user_id") String user_id
            ) {

        JSONObject object = new JSONObject();

        try{
            Upload fileUpload = uploadService.downloadFile(fileId, user_id);

            if (fileUpload == null) {
                object.put("error","A file with Id : "+ fileId + " could not be found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(object.toString());
            }
            return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(fileUpload.getFile_type()))
            .header(HttpHeaders.CONTENT_DISPOSITION,
"fileUpload; filename=\""+ fileUpload.getFile_name() +"\"")
            .body(new ByteArrayResource(fileUpload.getData()));

        }catch (Exception exception){
            object.put("error",exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(object.toString());
        }
    }
}
