package com.rental.PropertyRentalApi.Service.Impl;

import com.rental.PropertyRentalApi.Entity.PasswordOTP;
import com.rental.PropertyRentalApi.Entity.Users;
import com.rental.PropertyRentalApi.Repository.PasswordOTPRepository;
import com.rental.PropertyRentalApi.Repository.UserRepository;
import com.rental.PropertyRentalApi.Service.PasswordOTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

import static com.rental.PropertyRentalApi.Exception.ErrorsExceptionFactory.*;

@Service
@RequiredArgsConstructor
public class PasswordOTPServiceImpl implements PasswordOTPService {

    private final PasswordOTPRepository passwordOTPRepository;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void sendOTP(String email) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> notFound("User email not found."));

        String otp = String.format("%06d", new Random().nextInt(999999));

        PasswordOTP record = new PasswordOTP();
        record.setEmail(email);
        record.setOtpCode(otp);
        record.setCreatedAt(Instant.now());
        record.setExpiresAt(Instant.now().plusSeconds(300)); // 5 min expiry

        passwordOTPRepository.save(record);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your Password reset OTP");
        message.setText("Your OTP is: " + otp + "\nIt expires in 5 minutes.");

        javaMailSender.send(message);
    }

    @Override
    public boolean verifyOTP(String email, String otpCode) {

        PasswordOTP record = passwordOTPRepository
                .findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> notFound("No OTP found for this email."));

        if (record.getUsed()) {
            throw badRequest("OTP already used");
        }

        if (Instant.now().isAfter(record.getExpiresAt()))
            throw badRequest("OTP expired");

        if (!record.getOtpCode().equals(otpCode))
            throw validation("Invalid OTP");

        // Mark as used
        record.setUsed(true);
        passwordOTPRepository.save(record);

        return true;
    }

    @Override
    public void resetPassword(String email, String otpCode, String newPassword) {
        // Re-verify OTP before allowing password change
        // Note: OTP is already marked used in verifyOTP,
        // so you have two options:
        // A) Store a "verified" token after step 2 and use that here
        // B) Do verify + reset in one shot (simpler for now)

        PasswordOTP record = passwordOTPRepository
                .findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> notFound("No OTP found."));

        if (!record.getUsed())
            throw badRequest("OTP is not verified yet");

        if (!record.getOtpCode().equals(otpCode))
            throw validation("Invalid OTP");

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> notFound("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
