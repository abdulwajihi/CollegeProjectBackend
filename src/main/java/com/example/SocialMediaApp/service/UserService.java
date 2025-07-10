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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private AvatarService avatarService;




    @Transactional
    public User signUp(String username, String email, String password, String firstName, String lastName, List<String> preferences) {
        if (!PasswordValidator.isValid(password)) {
            throw new IllegalArgumentException("Password must be at least 8 characters long, contain an uppercase letter and a special character.");
        }
        if (userRepository.findByEmailOrUsernameWithPreferences(email).isPresent()) {
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
        user.setVerified(false);
        user.setTokenVersion(0);

        // ✅ Generate initials-based avatar and set path
        try {
            String avatarPath = avatarService.generateInitialsAvatar(firstName, lastName, username);
            user.setProfilePictureUrl(avatarPath); // User entity must have this field
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate avatar: " + e.getMessage());
        }
//        user = userRepository.save(user);
        List<String> validPreferenceNames = new ArrayList<>();

        for (String prefType : preferences) {
            PreferenceType type = preferenceTypeRepository.findByName(prefType)
                    .orElseThrow(() -> new RuntimeException("Invalid preference type: " + prefType));

            UserPreference pref = new UserPreference();
            pref.setPreferenceType(type);
            pref.setUser(user); // link to user

            pref.setPreferenceName(prefType);

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

    public boolean verifySignUp(String identifier, String otp) {
        boolean isEmail = identifier.contains("@gmail.com");
        User user = userRepository.findByEmailOrUsernameWithPreferences(identifier).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.isVerified()) {
            throw new RuntimeException("User already active");
        }
        boolean valid = otpService.verifyOtp(user, "SIGNUP", otp);
        if (valid) {
            user.setVerified(true);
            userRepository.save(user);
        }else{
            //If otp invalid or expired
            if(otpService.isOtpExpired(user,"Signup")){
                userRepository.delete(user);
            }
        }
        return valid;
    }

//        }
       public SigninResponse signIn(String identifier, String password){
        //chech if identifier is an email
           boolean isEmail = identifier.contains("@gmail.com");
           Optional<User> optionalUser= userRepository.findByEmailOrUsernameWithPreferences(identifier);
            User user =optionalUser.orElseThrow(()->new RuntimeException("User not found"));
            if(!user.isVerified()){
                throw new RuntimeException("User not active");
            }
            if(!passwordEncoder.matches(password,user.getPassword())){
                throw new RuntimeException("Invalid Password");
            }
           String token = jwtUtil.generateToken(user);
           // Fetch preferences
//           List<String> preferenceList = user.getPreferences().stream()
//                   .map(p -> p.getPreferenceType().getName())
//                   .collect(Collectors.toList());
           List<UserPreferenceProjection> prefProjections = preferenceRepository.getUserPreferencesWithNamesByUserId(user.getId());

           List<String> preferences = prefProjections.stream().map(UserPreferenceProjection::getPreferenceName).collect(Collectors.toList());

           return new SigninResponse(token,user,preferences);
//        Otp otp = otpService.genetareOtp(user, "SIGNIN", 5);
//        emailService.sendOtpEmail(user.getEmail(), otp.getOtpCode());
    }

//    public String verifySignIn(String email, String otp) {
//        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
//        boolean valid = otpService.verifyOtp(user, "SIGNIN", otp);
//        if (valid) {
//            return jwtUtil.generateToken(user);
//        }
//        throw new RuntimeException("Invalid OTP");
//    }

    public void resetPassword(String identifier) {
        User user = userRepository.findByEmailOrUsernameWithPreferences(identifier).orElseThrow(() -> new RuntimeException("User not found"));
        Otp otp = otpService.genetareOtp(user, "RESET_PASSWORD", 5);
        emailService.sendOtpEmail(identifier, otp.getOtpCode());
    }

    public void verifyResetPassword(String identifier, String otp, String newPassword) {
        User user = userRepository.findByEmailOrUsernameWithPreferences(identifier).orElseThrow(() -> new RuntimeException("User not found"));
        boolean valid = otpService.verifyOtp(user, "RESET_PASSWORD", otp);
        if (valid) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new RuntimeException("Invalid OTP");
        }
    }

    public void updatePassword(String identifier, String oldPassword, String newPassword) {
        User user = userRepository.findByEmailOrUsernameWithPreferences(identifier).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Invalid old password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void logoutAll(String identifier) {
        User user = userRepository.findByEmailOrUsernameWithPreferences(identifier).orElseThrow(() -> new RuntimeException("User not found"));
        user.setTokenVersion(user.getTokenVersion() + 1);
        userRepository.save(user);
    }
    public void resendOtp(String identifier, String purpose) {
        User user = userRepository.findByEmailOrUsernameWithPreferences(identifier).orElseThrow(() -> new RuntimeException("User not found"));

        // Check account activation based on purpose
        if (purpose.equals("SIGNUP") && user.isVerified()) {
            throw new RuntimeException("User already active.");
        }

        if ((purpose.equals("SIGNIN") || purpose.equals("RESET_PASSWORD")) && !user.isVerified()) {
            throw new RuntimeException("User is not active.");
        }

        Otp otp = otpService.genetareOtp(user, purpose, 5);
        emailService.sendOtpEmail(identifier, otp.getOtpCode());
    }
    public Long findIdByEmail(String identifier) {
        User user = userRepository.findByEmailOrUsernameWithPreferences(identifier)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + identifier));
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