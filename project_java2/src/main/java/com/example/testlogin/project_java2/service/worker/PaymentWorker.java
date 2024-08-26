package com.example.testlogin.project_java2.service.worker;
import com.example.testlogin.project_java2.dto.PaymentDto;
import com.example.testlogin.project_java2.dto.TemporaryDto;
import com.example.testlogin.project_java2.dto.UploadDto;
import com.example.testlogin.project_java2.mapper.PaymentMapper;
import com.example.testlogin.project_java2.model.*;
import com.example.testlogin.project_java2.model.object.PaymentToken;
import com.example.testlogin.project_java2.repo.*;
import com.example.testlogin.project_java2.security.Security;
import com.example.testlogin.project_java2.service.PaymentService;
import com.example.testlogin.project_java2.service.TemporaryService;
import com.example.testlogin.project_java2.service.UploadService;
import net.minidev.json.JSONObject;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PaymentWorker implements PaymentService, TemporaryService {


    private final UploadRepo uploadRepo;
    private final UserRepo userRepo;
    private final BankAccountRepo bankAccountRepo;
    private final PaymentRepo paymentRepo;
    private final OcrWorker ocrWorker;
    private final BankAccountWorker bankAccountWorker;
    private final UploadService uploadService;
    private  final TemporaryRepo temporaryRepo;

    @Autowired
    public PaymentWorker(UploadRepo uploadRepo, UserRepo userRepo, BankAccountRepo bankAccountRepo, PaymentRepo paymentRepo, OcrWorker ocrWorker, BankAccountWorker bankAccountWorker, UploadService uploadService, TemporaryRepo temporaryRepo) {
        this.uploadRepo = uploadRepo;
        this.userRepo = userRepo;
        this.bankAccountRepo = bankAccountRepo;
        this.paymentRepo = paymentRepo;
        this.ocrWorker = ocrWorker;
        this.bankAccountWorker = bankAccountWorker;
        this.uploadService = uploadService;
        this.temporaryRepo = temporaryRepo;
    }

    @Override
    public List<PaymentDto> listPaymentApi() {

        List<Payment> payments = paymentRepo.findAll();

        return  payments.stream()
            .map(PaymentMapper::mapToPaymentApiDto)
            .collect(Collectors.toList());
    }


    @Override
    public PaymentDto create(File file, JSONObject object,
                             UploadDto uploadDto, BankAccount bankAccountUser,
                             String account_number_sent_to
    ) throws TesseractException {

        String dataScan = ocrWorker.getImageString(file);

        String inherited_account_number = find_Inherited_account_number_test(dataScan);
        double amount = find_amount_test(dataScan);
        String content = find_content_test(dataScan);
        PaymentDto newPaymentDto = new PaymentDto();

        if (account_number_sent_to == null ||
        !account_number_sent_to.matches("[0-9]+")) {
            newPaymentDto.setId(null);
            newPaymentDto.setUser_bank_code(account_number_sent_to);
            newPaymentDto.setDeposit_amount(amount);
            newPaymentDto.setPayment_content(content);
            newPaymentDto.setBankAccount(null);
            newPaymentDto.setStatus_check_inherited_account_number(false);
            newPaymentDto.setStatus_check_content(false);
            newPaymentDto.setStatus_increase_amount(false);
            object.put("payment_status",false);
            object.put("error","Your bank code is invalid!");

            return newPaymentDto;
        }

        if(bankAccountUser == null){
            newPaymentDto.setId(null);
            newPaymentDto.setUser_bank_code(account_number_sent_to);
            newPaymentDto.setDeposit_amount(amount);
            newPaymentDto.setPayment_content(content);
            newPaymentDto.setBankAccount(null);
            newPaymentDto.setStatus_check_inherited_account_number(false);
            newPaymentDto.setStatus_check_content(false);
            newPaymentDto.setStatus_increase_amount(false);
            object.put("payment_status",false);
            object.put("error","Open app to payment!");

            return newPaymentDto;
        }
        System.out.println("bankAccountUser: " + bankAccountUser.getId());
        System.out.println("account_number_sent_to: " + account_number_sent_to);
        System.out.println("inherited_account_number: " + inherited_account_number);
        System.out.println("amount: " + amount);
        System.out.println("content: " + content);

        if(bankAccountWorker.increase_amount_bank_account(amount,
            account_number_sent_to, bankAccountUser)){

            Payment userPayment = new Payment();
            Optional<Upload> uploadNew = uploadRepo.findById(uploadDto.getId());

            userPayment.setUser_bank_code(account_number_sent_to);
            userPayment.setDeposit_amount(amount);
            userPayment.setPayment_content(content);
            userPayment.setBankAccount(bankAccountUser);
            uploadNew.ifPresent(userPayment::setUpload);
            Payment newPaymentByUser = paymentRepo.save(userPayment);

            newPaymentDto.setId(newPaymentByUser.getId());
            newPaymentDto.setUser_bank_code(newPaymentByUser.getUser_bank_code());
            newPaymentDto.setStart_time(newPaymentByUser.getStart_time());
            newPaymentDto.setDeposit_amount(newPaymentByUser.getDeposit_amount());
            newPaymentDto.setPayment_content(newPaymentByUser.getPayment_content());
            uploadNew.ifPresent(newPaymentDto::setUpload);
            Optional<BankAccount> bankAccountUpdated = bankAccountRepo.findById(bankAccountUser.getId());
            bankAccountUpdated.ifPresent(newPaymentDto::setBankAccount);
            deletePaymentTokenByUserId(userRepo.findByEmail(Security.getSessionUser()).getId());

            newPaymentDto.setStatus_check_inherited_account_number(true);
            newPaymentDto.setStatus_check_content(true);
            newPaymentDto.setStatus_increase_amount(true);
            object.put("payment_status",true);
            return newPaymentDto;
        }

        newPaymentDto.setId(null);
        newPaymentDto.setUser_bank_code(account_number_sent_to);
        newPaymentDto.setDeposit_amount(amount);
        newPaymentDto.setPayment_content(content);
        newPaymentDto.setBankAccount(bankAccountUser);
        newPaymentDto.setStatus_check_inherited_account_number(false);
        newPaymentDto.setStatus_check_content(false);
        newPaymentDto.setStatus_increase_amount(false);
        object.put("error","error payment!");
        object.put("payment_status",false);

        return newPaymentDto;
    }

    public boolean check_inherited_account_number(String inherited_account_number){
        System.out.println("So sánh inherited_account_number: --" + inherited_account_number + "-- với --020089379998--");
        return Objects.equals(inherited_account_number, "020089379998");//104234245541
    }

    @Override
    public boolean check_content(String content){
        String token = findPaymentTokenByUserId(userRepo.findByEmail(Security.getSessionUser()).getId());
        return Objects.equals(content, token);
    }

    @Override
    public void savePaymentToken(List<PaymentToken> paymentToken,
                                 HttpServletRequest request,
                                 String token, HttpSession session) {

        if (paymentToken == null) {
            paymentToken = new ArrayList<>();
            request.getSession().setAttribute("paymentToken", paymentToken);
        }
        PaymentToken paymentTokenObj = new PaymentToken(token);
        paymentToken.add(paymentTokenObj);
        request.getSession().setAttribute("paymentToken", paymentToken);
        List<PaymentToken> paymentTOKEN = (List<PaymentToken>) session.getAttribute("paymentToken");
        for(PaymentToken token_ : paymentTOKEN){
            System.out.println("Token đã lưu vao session: "+token_.getToken());
        }
        // Need persist to db
    }

    @Override
    public String generate_token() {

        return "1188033643";//Product234234fsfsf3r3
    }

    private String find_number_send(String data) {
        Pattern pattern = Pattern.compile("Account number sent to:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public String find_Inherited_account_number(String data) {
        Pattern pattern = Pattern.compile("Inherited account number:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private double find_amount(String data) {
        Pattern pattern = Pattern.compile("Amount:\\s*([\\d.,]+)\\s*VND");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            String amountStr = matcher.group(1).replaceAll("[,.]", "");
            return Double.parseDouble(amountStr);
        }
        return 0.0;
    }

    public String find_content(String data) {
        Pattern pattern = Pattern.compile("Content:\\s*(.+)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    @Override
    public double find_amount_test(String data) {
        Pattern pattern = Pattern.compile("(\\d{1,3}(?:,\\d{3})*(?:\\.\\d+)?)\\s*VND");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            String amountStr = matcher.group(1).replaceAll(",", "");
            return Double.parseDouble(amountStr);
        }
        return 0.0;
    }

    @Override
    public String find_content_test(String data) {
        Pattern pattern = Pattern.compile("lo TH] \\s*(.+)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    @Override
    public String find_Inherited_account_number_test(String data) {
        Pattern pattern = Pattern.compile("Tài khoản thụ hưởng \\s*(.+)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    @Override
    public void deleteOldPaymentToken(LocalDateTime timeLimit) {
        temporaryRepo.deleteByCreatedAtBefore(timeLimit);
        System.err.println("Handle payment token...");
    }

    @Override
    public int count() {
        return (int) temporaryRepo.count();
    }

    @Override
    public int countTemporariesByUserAccount(UserAccount userAccount) {
        return temporaryRepo.countTemporariesByUserAccount(userAccount);
    }

    @Override
    public TemporaryDto savePaymentToken(String token) {
        UserAccount userAccount = userRepo.findByEmail(Security.getSessionUser());
        Temporary temporary = new Temporary();
        temporary.setPayment_token(token);
        temporary.setUserAccount(userAccount);
        Temporary temporary_new = temporaryRepo.save(temporary);
        TemporaryDto temporaryDto = new TemporaryDto();
        temporaryDto.setId(temporary_new.getId());
        temporaryDto.setPayment_token(temporary_new.getPayment_token());
        temporaryDto.setUserAccount(temporary_new.getUserAccount());

        return temporaryDto;
    }


    @Override
    public void deletePaymentTokenByUserId(String user_id) {
        temporaryRepo.deletePaymentTokenByUserId(user_id);
    }

    @Override
    public String findPaymentTokenByUserId(String user_id) {

        String payment_Token = temporaryRepo.findPaymentTokenByUserId(user_id);
        TemporaryDto temporaryDto = new TemporaryDto();
        temporaryDto.setPayment_token(payment_Token);
        return temporaryDto.getPayment_token();
    }
}
