package com.example.SocialMediaApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

//@Service
//public class EmailService {
//    @Autowired
//    JavaMailSender javaMailSender;
//    @Async
//   public void sendOtpEmail(String to,String otp){
//       SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setFrom("verified-gmail");
//       mailMessage.setTo(to);
//       mailMessage.setSubject("Your Otp code: ");
//       mailMessage.setText("Your otp code is "+otp);
//       javaMailSender.send(mailMessage);
//   }
//}
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.from}")
    private String fromEmail;

    @Async
    public void sendOtpEmail(String to, String otp) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail); // Use verified domain email
            mailMessage.setTo(to);
            mailMessage.setSubject("Your OTP Code");
            mailMessage.setText("Your OTP code is " + otp);
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            System.err.println("Email sending failed: " + e.getMessage());
        }
    }
}

