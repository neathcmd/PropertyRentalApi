package com.rental.PropertyRentalApi.Service;

public interface PasswordOTPService {

    void sendOTP(String email);

    boolean verifyOTP(String email, String otpCode);

    void resetPassword(String email, String otpCode, String newPassword);
}
