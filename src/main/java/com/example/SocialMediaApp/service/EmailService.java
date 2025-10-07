package com.example.SocialMediaApp.service;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
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
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    @Value("${spring.mail.from}")
//    private String fromEmail;
//
//    @Async
//    public void sendOtpEmail(String to, String otp) {
//        try {
//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//            mailMessage.setFrom(fromEmail); // Use verified domain email
//            mailMessage.setTo(to);
//            mailMessage.setSubject("Your OTP Code");
//            mailMessage.setText("Your OTP code is " + otp);
//            javaMailSender.send(mailMessage);
//        } catch (Exception e) {
//            System.err.println("Email sending failed: " + e.getMessage());
//        }
//    }
//}
import com.sendgrid.*;

@Service
public class EmailService {

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Value("${MAIL_USERNAME}")
    private String fromEmail;

    @Async
    public void sendOtpEmail(String to, String otp) {
        Email from = new Email(fromEmail);
        String subject = "Your OTP Code";
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", "Your OTP code is: " + otp);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println("SendGrid Status: " + response.getStatusCode());
        } catch (Exception e) {
            System.err.println("SendGrid API error: " + e.getMessage());
        }
    }
}


