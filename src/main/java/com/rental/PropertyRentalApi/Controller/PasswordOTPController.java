package com.rental.PropertyRentalApi.Controller;

import com.rental.PropertyRentalApi.DTO.request.ResetPasswordRequest;
import com.rental.PropertyRentalApi.DTO.request.VerifyOTPRequest;
import com.rental.PropertyRentalApi.Service.PasswordOTPService;
import com.rental.PropertyRentalApi.DTO.request.ForgotPasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class PasswordOTPController {

    private final PasswordOTPService passwordOTPService;

    // Endpoint 1: Send OTP
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordOTPService.sendOTP(request.getEmail());
        return ResponseEntity.ok("OTP sent to your email");
    }

    // Endpoint 2: Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestBody VerifyOTPRequest request) {
        passwordOTPService.verifyOTP(request.getEmail(), request.getOtpCode());
        return ResponseEntity.ok("OTP verified successfully");
    }

    // Endpoint 3: Reset password (optional, user can skip)
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordOTPService.resetPassword(request.getEmail(), request.getOtpCode(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successfully");
    }

}


// The Flow in Action
//
// POST /api/auth/forgot-password   { "email": "user@example.com" }
//         ↓ generates OTP, saves to DB, sends email
//
// POST /api/auth/verify-otp        { "email": "...", "otpCode": "123456" }
//         ↓ validates OTP, marks as used, user is now "authenticated"
//
// POST /api/auth/reset-password    { "email": "...", "otpCode": "...", "newPassword": "..." }
//         ↓ (optional - user can skip this step per your spec)
