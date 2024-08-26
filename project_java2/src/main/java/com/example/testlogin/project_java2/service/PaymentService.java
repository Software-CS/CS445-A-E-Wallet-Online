package com.example.testlogin.project_java2.service;

import com.example.testlogin.project_java2.dto.PaymentDto;
import com.example.testlogin.project_java2.dto.UploadDto;
import com.example.testlogin.project_java2.model.BankAccount;
import com.example.testlogin.project_java2.model.Upload;
import com.example.testlogin.project_java2.model.object.PaymentToken;
import net.minidev.json.JSONObject;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface PaymentService {


    List<PaymentDto> listPaymentApi();

    PaymentDto create(File file,
                      JSONObject object,
                      UploadDto uploadDto,
                      BankAccount bankAccountUser,
                      String account_number_sent_to
    )
    throws TesseractException, IOException;


    String generate_token();

    boolean check_inherited_account_number(String inherited_account_number);

    boolean check_content(String content);

    void savePaymentToken(List<PaymentToken> paymentToken,
                          HttpServletRequest request,
                          String token, HttpSession session);

    String find_Inherited_account_number(String data);

    String find_content(String data);

    double find_amount_test(String data);

    String find_content_test(String data);

    String find_Inherited_account_number_test(String data);
}
