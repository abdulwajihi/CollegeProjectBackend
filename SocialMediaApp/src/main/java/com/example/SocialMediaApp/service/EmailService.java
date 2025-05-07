package com.example.SocialMediaApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    JavaMailSender javaMailSender;
   public void sendOtpEmail(String to,String otp){
       SimpleMailMessage mailMessage = new SimpleMailMessage();
       mailMessage.setTo(to);
       mailMessage.setSubject("Your Otp code: ");
       mailMessage.setText("Your otp code is "+otp);
       javaMailSender.send(mailMessage);
   }
}
