package com.example.SocialMediaApp.service;

import com.example.SocialMediaApp.dto.*;
import com.example.SocialMediaApp.repository.BlacklistedTokenRepository;
import com.example.SocialMediaApp.repository.PreferenceRepository;
import com.example.SocialMediaApp.repository.PreferenceTypeRepository;
import com.example.SocialMediaApp.repository.UserRepository;
import com.example.SocialMediaApp.util.JwtUtil;
import com.example.SocialMediaApp.util.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PreferenceRepository preferenceRepository;

    @Autowired
    private OTPService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PreferenceTypeRepository preferenceTypeRepository;

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;





    public User signUp(String username, String email, String password, String firstName, String lastName, String gender, List<String> preferences) {
        if (!PasswordValidator.isValid(password)) {
            throw new IllegalArgumentException("Password must be at least 8 characters long, contain an uppercase letter and a special character.");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        if (preferences.size() > 5) {
            throw new RuntimeException("You can select up to 5 preferences only.");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setGender(gender);
        user.setVerified(false);
        user.setTokenVersion(0);
//        user = userRepository.save(user);
        List<String> validPreferenceNames = new ArrayList<>();

        for (String prefType : preferences) {
            PreferenceType type = preferenceTypeRepository.findByName(prefType)
                    .orElseThrow(() -> new RuntimeException("Invalid preference type: " + prefType));

            UserPreference pref = new UserPreference();
            pref.setPreferenceType(type);
            pref.setUser(user); // link to user

            user.getPreferences().add(pref); // add to list (will be saved due to cascade)

            validPreferenceNames.add(prefType);
        }

        user.setPreferenceNames(String.join(",", validPreferenceNames));

// Only ONE save needed — saves user + preferences
        userRepository.save(user);

        Otp otp = otpService.genetareOtp(user, "SIGNUP", 3);
        emailService.sendOtpEmail(email, otp.getOtpCode());
        return user;
    }

    public boolean verifySignUp(String email, String otp) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.isVerified()) {
            throw new RuntimeException("User already active");
        }
        boolean valid = otpService.verifyOtp(user, "SIGNUP", otp);
        if (valid) {
            user.setVerified(true);
            userRepository.save(user);
        }
        return valid;
    }

//        }
       public void signIn(String identifier,String password){
        //chech if identifier is an email
           boolean isEmail = identifier.contains("@gmail.com");
           Optional<User> optionalUser= isEmail ? userRepository.findByEmail(identifier) : userRepository.findByUsername(identifier);
            User user =optionalUser.orElseThrow(()->new RuntimeException("User not found"));
            if(!user.isVerified()){
                throw new RuntimeException("User not active");
            }
            if(!passwordEncoder.matches(password,user.getPassword())){
                throw new RuntimeException("Invalid Password");
            }
        Otp otp = otpService.genetareOtp(user, "SIGNIN", 5);
        emailService.sendOtpEmail(user.getEmail(), otp.getOtpCode());
    }

    public String verifySignIn(String email, String otp) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        boolean valid = otpService.verifyOtp(user, "SIGNIN", otp);
        if (valid) {
            return jwtUtil.generateToken(user);
        }
        throw new RuntimeException("Invalid OTP");
    }

    public void resetPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Otp otp = otpService.genetareOtp(user, "RESET_PASSWORD", 5);
        emailService.sendOtpEmail(email, otp.getOtpCode());
    }

    public void verifyResetPassword(String email, String otp, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        boolean valid = otpService.verifyOtp(user, "RESET_PASSWORD", otp);
        if (valid) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new RuntimeException("Invalid OTP");
        }
    }

    public void updatePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Invalid old password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void logoutAll(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        user.setTokenVersion(user.getTokenVersion() + 1);
        userRepository.save(user);
    }
    public void resendOtp(String email, String purpose) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        // Check account activation based on purpose
        if (purpose.equals("SIGNUP") && user.isVerified()) {
            throw new RuntimeException("User already active.");
        }

        if ((purpose.equals("SIGNIN") || purpose.equals("RESET_PASSWORD")) && !user.isVerified()) {
            throw new RuntimeException("User is not active.");
        }

        Otp otp = otpService.genetareOtp(user, purpose, 5);
        emailService.sendOtpEmail(email, otp.getOtpCode());
    }
    public Long findIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return user.getId();
    }

    public void logoutCurrentToken(String token) {
        if (blacklistedTokenRepository.existsByToken(token)) {
            throw new RuntimeException("Token already blacklisted.");
        }

        BlacklistedToken blacklisted = new BlacklistedToken();
        blacklisted.setToken(token);
        blacklisted.setExpiryDate(jwtUtil.getExpiryFromToken(token));

        blacklistedTokenRepository.save(blacklisted);
    }



}