package com.example.testlogin.project_java2.controller.api.user;


import com.example.testlogin.project_java2.dto.BankAccountDto;
import com.example.testlogin.project_java2.dto.TemporaryDto;
import com.example.testlogin.project_java2.dto.UserResponseDto;
import com.example.testlogin.project_java2.model.BankAccount;
import com.example.testlogin.project_java2.model.UserAccount;
import com.example.testlogin.project_java2.model.object.PaymentToken;
import com.example.testlogin.project_java2.security.Security;
import com.example.testlogin.project_java2.security.middleware.JWTAuthenticationFilter;
import com.example.testlogin.project_java2.service.BankAccountService;
import com.example.testlogin.project_java2.service.PaymentService;
import com.example.testlogin.project_java2.service.TemporaryService;
import com.example.testlogin.project_java2.service.UserService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserGetApiController {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;
    private final BankAccountService bankAccountService;
    private final PaymentService paymentService;
    private final TemporaryService temporaryService;

    @Autowired
    public UserGetApiController(JWTAuthenticationFilter jwtAuthenticationFilter, UserService userService, BankAccountService bankAccountService, PaymentService paymentService, TemporaryService temporaryService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userService = userService;
        this.bankAccountService = bankAccountService;
        this.paymentService = paymentService;
        this.temporaryService = temporaryService;
    }

    @GetMapping("/profile")
    private ResponseEntity<?> user(
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest request
    ){
        JSONObject object = new JSONObject();
        try{
            String token = jwtAuthenticationFilter.getJWTFromRequest(request);
            String userId = userService.findByEmail(userDetails.getUsername()).getId();
            UserResponseDto auth = new UserResponseDto(userId, token, userDetails);

            System.out.println("Get user login: " + auth);

            return new ResponseEntity<>(auth, HttpStatus.OK);
        }catch (Exception exception){
            object.put("error",exception);
            return new ResponseEntity<>(object, HttpStatus.OK);
        }
    }

    @GetMapping("/active_bank_account")
    private ResponseEntity<JSONObject> active_bank_account(){

        JSONObject object = new JSONObject();
        try{
            UserAccount user = userService.findByEmail(Security.getSessionUser());
            BankAccountDto bankAccountDto =  bankAccountService.active_bank_account(user);
            if(bankAccountDto != null){
                object.put("message","Active bank account successfully");
                object.put("status",true);
                object.put("bankAccount",bankAccountDto);
                return new ResponseEntity<>(object, HttpStatus.OK);
            }
            object.put("status",false);
            object.put("message","Active bank account failed!!!");
            return new ResponseEntity<>(object, HttpStatus.OK);
        }catch (Exception exception){
            object.put("error", exception.getMessage());
            return new ResponseEntity<>(object, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/bank_account")
    private ResponseEntity<?> bank_account(
            @AuthenticationPrincipal UserDetails userDetails){
        JSONObject res = new JSONObject();
        try{
            UserAccount user = userService.findByEmail(userDetails.getUsername());
            BankAccountDto bankAccountDto = bankAccountService.getBankAccountDtoByUser(user);

            return new ResponseEntity<>(bankAccountDto, HttpStatus.OK);
        }catch (Exception exception){
            res.put("error",exception);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
    }

    @GetMapping("/get_payment_token")
    private ResponseEntity<?> get_payment_token(){

        JSONObject object = new JSONObject();
        try{
            String payment_token = temporaryService.findPaymentTokenByUserId(
            userService.findByEmail(Security.getSessionUser()).getId());
            object.put("payment_token", payment_token);
            return new ResponseEntity<>(object, HttpStatus.OK);

        }catch (Exception exception){
            object.put("error", exception);
            return new ResponseEntity<>(object, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/generate_payment_token")
    private ResponseEntity<?> generate_payment_token(HttpServletRequest request) throws IOException {

        System.err.println("This request id is: " + request.getSession().getId());
        JSONObject object = new JSONObject();
        try{
            String token = paymentService.generate_token();
            if(temporaryService.countTemporariesByUserAccount(
            userService.findByEmail(Security.getSessionUser())) >= 1){
                object.put("message" , "You did create a payment token before!");
                return new ResponseEntity<>(object, HttpStatus.BAD_REQUEST);
            }
            TemporaryDto temporaryDto = temporaryService.savePaymentToken(token);
            object.put("message" , "To verify the payment invoice, You must enter the token into your payment content!");
            object.put("data" ,temporaryDto);

            return ResponseEntity.ok(object);
        }catch  (Exception exception){
            object.put("error", exception.getMessage());
            return new ResponseEntity<>(object, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
