package com.example.SocialMediaApp.controller;

import com.example.SocialMediaApp.dto.*;
import com.example.SocialMediaApp.service.UserService;
import com.example.SocialMediaApp.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")

public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    JwtUtil jwtUtil;


    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignupRequest request) {
        userService.signUp(request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                request.getGender(),
                request.getPreferences());
        return ResponseEntity.ok("Sign-up successful. Please check your email for OTP.");
    }

    @PostMapping("/verify-signup")
    public ResponseEntity<String> verifySignUp(
            @RequestBody VerifyOtpRequest request) {
        boolean success = userService.verifySignUp(request.getEmail(), request.getOtp());
        return success ? ResponseEntity.ok("Account activated.") : ResponseEntity.badRequest().body("Invalid OTP.");
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(
            @RequestBody SigninRequest request) {
        userService.signIn(request.getUsername(), request.getPassword());
        return ResponseEntity.ok("OTP sent to your email.");
    }

    @PostMapping("/verify-signin")
    public ResponseEntity<String> verifySignIn(
            @RequestBody VerifySigninRequest request) {
        String token = userService.verifySignIn(request.getEmail(), request.getOtp());
        return ResponseEntity.ok("JWT Token: " + token);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request.getEmail());
        return ResponseEntity.ok("Password reset OTP sent.");
    }

    @PostMapping("/verify-reset-password")
    public ResponseEntity<String> verifyResetPassword(
          @RequestBody VerifyResetPasswordRequest request ) {
        userService.verifyResetPassword(request.getEmail(), request.getOtp(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successful.");
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(
           @RequestBody UpdatePasswordRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        userService.updatePassword(email,request.getOldPassword(),request.getNewPassword());
        return ResponseEntity.ok("Password updated.");
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            return ResponseEntity.badRequest().body("No token provided");
        }

        userService.logoutCurrentToken(token);
        SecurityContextHolder.clearContext(); // Clear security context

        return ResponseEntity.ok("Logged out from current session");
    }



    @PostMapping("/logout-all")
    public ResponseEntity<String> logoutAll() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        userService.logoutAll(email);
        return ResponseEntity.ok("Logged out from all devices.");
    }
    @PostMapping("/resend-signup-otp")
    public ResponseEntity<String> resendSignupOtp(@RequestBody ResendPasswordRequest request) {
        userService.resendOtp(request.getEmail(), "SIGNUP");
        return ResponseEntity.ok("Sign-up OTP resent.");
    }

    @PostMapping("/resend-signin-otp")
    public ResponseEntity<String> resendSigninOtp(@RequestBody ResendPasswordRequest request) {
        userService.resendOtp(request.getEmail(), "SIGNIN");
        return ResponseEntity.ok("Sign-in OTP resent.");
    }

    @PostMapping("/resend-reset-password-otp")
    public ResponseEntity<String> resendResetPasswordOtp(@RequestBody ResendPasswordRequest request) {
        userService.resendOtp(request.getEmail(), "RESET_PASSWORD");
        return ResponseEntity.ok("Reset password OTP resent.");
    }


}