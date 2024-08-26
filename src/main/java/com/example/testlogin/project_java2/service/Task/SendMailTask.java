package com.example.testlogin.project_java2.service.Task;

import com.example.testlogin.project_java2.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMailTask implements SendMailService {

    private final JavaMailSender mailSender;

    @Autowired
    public SendMailTask(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void setMailSender(String toEmail, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("lol00sever@gmail.com");
        message.setTo(toEmail);
        message.setText(content);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("Send to " + toEmail);
    }
}
