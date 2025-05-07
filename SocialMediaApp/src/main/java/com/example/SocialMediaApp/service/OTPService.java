package com.example.SocialMediaApp.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import java.util.Random;
//
//@Service
//public class OTPService {
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    public String generateOTP() {
//        return String.valueOf(new Random().nextInt(999999));
//    }
//
//    public void sendOTPEmail(String toEmail, String otp) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(toEmail);
//        message.setSubject("Your OTP Code");
//        message.setText("Your OTP is: " + otp);
//        mailSender.send(message);
//    }
//}
//

import com.example.SocialMediaApp.dto.Otp;
import com.example.SocialMediaApp.dto.User;
import com.example.SocialMediaApp.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OTPService {
    @Autowired
    OtpRepository otpRepository;

     public Otp genetareOtp(User user,String purpose,int expirationMinutes){
      List<Otp> existingOtps = otpRepository.findByUserAndPurpose(user,purpose);
       otpRepository.deleteAll(existingOtps);
       String otpcode =  generateOtpCode();
      LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(expirationMinutes);
       Otp otp = new Otp();
       otp.setUser(user);
       otp.setOtpCode(otpcode);
       otp.setPurpose(purpose);
       otp.setExpirationTime(expirationTime);
       return otpRepository.save(otp);
  }
    public boolean verifyOtp(User user, String purpose, String otpCode) {//verify otp
        List<Otp> otps = otpRepository.findByUserAndPurpose(user, purpose);
        for (Otp otp : otps) {
            if (otp.getOtpCode().equals(otpCode) && LocalDateTime.now().isBefore(otp.getExpirationTime())) {
                otpRepository.delete(otp);
                return true;
            }
        }
        return false;
    }

  private String generateOtpCode(){
      SecureRandom random = new SecureRandom();
      int code = 100000+ random.nextInt(900000);
       return String.valueOf(code);
  }
}
